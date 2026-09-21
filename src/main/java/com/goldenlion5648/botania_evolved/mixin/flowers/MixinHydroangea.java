package com.goldenlion5648.botania_evolved.mixin.flowers;

import com.goldenlion5648.botania_evolved.helpers.HydroangeaBsideState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.flower.generating.FluidGeneratorBlockEntity;
import vazkii.botania.common.block.flower.generating.HydroangeasBlockEntity;

import java.util.*;


@Mixin(HydroangeasBlockEntity.class)
public abstract class MixinHydroangea extends FluidGeneratorBlockEntity {

    private boolean isBside = true;

    private int currentOrderIndex = 0;
    private List<BlockPos> currentAdjOrder = new ArrayList<>();
    private HydroangeaBsideState currentState = HydroangeaBsideState.NEEDS_RESET;

    private static final String TAG_CUR_ORDER_INDEX = "currentOrderIndex";
    private static final String TAG_CUR_ADJ_ORDER = "currentAdjOrder";
    private static final String TAG_STATE_MACHINE_STATE = "stateMachineState";

    private final List<Integer> STREAK_OUTPUTS = List.of(
            2, 5, 12, 30, 80, 200, 500, 1200
    );

    private final Map<BlockPos, Integer> offsetToComparatorSignal = Map.of(
            new BlockPos(0, 0, -1), 1,
            new BlockPos(1, 0, -1), 2,
            new BlockPos(1, 0, 0), 3,
            new BlockPos(1, 0, 1), 4,
            new BlockPos(0, 0, 1), 5,
            new BlockPos(-1, 0, 1), 6,
            new BlockPos(-1, 0, 0), 7,
            new BlockPos(-1, 0, -1), 8
    );

    protected MixinHydroangea(BlockEntityType<?> type, BlockPos pos, BlockState state, TagKey<Fluid> consumedFluid, int startBurnTime, int manaPerTick) {
        super(type, pos, state, consumedFluid, startBurnTime, manaPerTick);
    }

    void resetBSide() {
        currentAdjOrder = Arrays.asList(offsetToComparatorSignal.keySet().toArray(new BlockPos[0]));
        Collections.shuffle(currentAdjOrder);
        currentOrderIndex = 0;
        currentState = HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT;
    }

    void gotoCorrectStateAfterBeingWrong() {
        if (currentOrderIndex == 0) {
            currentState = HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT;
        } else {
            currentState = HydroangeaBsideState.NEEDS_RESET;
        }
    }

    boolean isWaterAtOffsetIndex(int indexToCheck) {
        BlockPos pos = getEffectivePos().offset(currentAdjOrder.get(indexToCheck));
        FluidState curFluidState = getLevel().getFluidState(pos);
        TagKey<Fluid> fluidToEat = ((MixinFluidGeneratorBlock) this).getConsumedFluid();

        return curFluidState.is(fluidToEat) && curFluidState.isSource();
    }

    @Inject(method = "tickFlower", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void customTickFlower(CallbackInfo ci) {
        // We cancel no matter what since the only hydroangea specific code that runs
        // is related to decay, which we do ourselves in the flower decay mixin.
        ci.cancel();
        if (!isBside) {
            // Do normal hydroangea stuff
            return;
        }
        burnTime = -1;
        cooldown = 10;
        super.tickFlower();

        if (currentState == HydroangeaBsideState.NEEDS_RESET) {
            resetBSide();
        }
        if (currentState == HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT) {
            for (int i = 0; i < offsetToComparatorSignal.size(); i++) {
                if (!isWaterAtOffsetIndex(i)) {
                    return;
                }
            }
            currentState = HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT;
        }
        if (currentState == HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT || currentState == HydroangeaBsideState.WAITING_FOR_SPOT_TO_DISAPPEAR) {
            // Check that all positions before current pos are not water
            for (int i = 0; i < currentOrderIndex; i++) {
                if (isWaterAtOffsetIndex(i)) {
                    gotoCorrectStateAfterBeingWrong();
                    return;
                }
            }
            // Check that all positions including and after current pos are water
            for (int i = currentOrderIndex + 1; i < offsetToComparatorSignal.size(); i++) {
                if (!isWaterAtOffsetIndex(i)) {
                    gotoCorrectStateAfterBeingWrong();
                    return;
                }
            }
        }

        if (currentState == HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT) {
            if (isWaterAtOffsetIndex(currentOrderIndex)) {
                currentState = HydroangeaBsideState.WAITING_FOR_SPOT_TO_DISAPPEAR;
            } else {
                gotoCorrectStateAfterBeingWrong();
            }
        }

        if (currentState == HydroangeaBsideState.WAITING_FOR_SPOT_TO_DISAPPEAR) {
            if (!isWaterAtOffsetIndex(currentOrderIndex)) {
                addMana(STREAK_OUTPUTS.get(currentOrderIndex));
                setChanged();
                sync();
                playSound();
                currentOrderIndex += 1;
                if (currentOrderIndex == currentAdjOrder.size()) {
                    currentState = HydroangeaBsideState.NEEDS_RESET;
                } else {
                    currentState = HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT;
                }
            }
        }
    }

    @Override
    public int getMaxMana() {
        if (isBside) {
            return STREAK_OUTPUTS.get(STREAK_OUTPUTS.size() - 1);
        }
        return 150;
    }

    @Override
    public int getComparatorSignal() {
        if (!isBside) {
            return 0;
        }

        if (currentState == HydroangeaBsideState.NEEDS_RESET || currentState == HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT) {
            return 0;
        }

        if (currentAdjOrder == null) {
            return 0;
        }
        return offsetToComparatorSignal.getOrDefault(currentAdjOrder.get(currentOrderIndex), 15);
    }

    @Inject(method = "writeToPacketNBT", at = @At("RETURN"), remap = false)
    public void writeToPacketNBT(CompoundTag cmp, CallbackInfo ci) {
        cmp.putInt(TAG_CUR_ORDER_INDEX, currentOrderIndex);
        cmp.putString(TAG_STATE_MACHINE_STATE, currentState.name());
        cmp.putLongArray(TAG_CUR_ADJ_ORDER, currentAdjOrder.stream().map(pos -> pos.asLong()).toList());
    }

    @Inject(method = "readFromPacketNBT", at = @At("RETURN"), remap = false)
    public void readFromPacketNBT(CompoundTag cmp, CallbackInfo ci) {
        currentState = HydroangeaBsideState.valueOf(cmp.getString(TAG_STATE_MACHINE_STATE));
        currentOrderIndex = cmp.getInt(TAG_CUR_ORDER_INDEX);
        var positionsAsLongsList = cmp.getLongArray(TAG_CUR_ADJ_ORDER);
        currentAdjOrder.clear();
        for (int i = 0; i < offsetToComparatorSignal.size(); i++) {
            currentAdjOrder.add(BlockPos.of(positionsAsLongsList[i]));
        }
    }
}
