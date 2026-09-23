//package com.goldenlion5648.botania_evolved.custom_flowers;
//
//import com.goldenlion5648.botania_evolved.helpers.HydroangeaBsideState;
//import com.goldenlion5648.botania_evolved.mixin.flowers.MixinFluidGeneratorBlock;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.FluidState;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
//import vazkii.botania.api.block_entity.RadiusDescriptor;
//import vazkii.botania.client.fx.WispParticleData;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//public class HydroangeaBSideBlockEntity extends GeneratingFlowerBlockEntity {
//    public HydroangeaBSideBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//    public HydroangeaBSideBlockEntity(BlockPos pos, BlockState state) {
//        super(ModTileEntities.Blocks.Flowers.HYDROANGEA_BSIDE.get(), pos, state);
//    }
//
//    private boolean isBside = true;
//
//    private int currentOrderIndex = 0;
//    private List<BlockPos> currentAdjOrder = new ArrayList<>();
//    private HydroangeaBsideState currentState = HydroangeaBsideState.NEEDS_RESET;
//
//    private static final String TAG_CUR_ORDER_INDEX = "currentOrderIndex";
//    private static final String TAG_CUR_ADJ_ORDER = "currentAdjOrder";
//    private static final String TAG_STATE_MACHINE_STATE = "stateMachineState";
//
//    private final List<Integer> STREAK_OUTPUTS = List.of(
//            2, 5, 12, 30, 80, 200, 500, 1200
//    );
//
//    private final Map<BlockPos, Integer> offsetToComparatorSignal = Map.of(
//            new BlockPos(0, 0, -1), 1,
//            new BlockPos(1, 0, -1), 2,
//            new BlockPos(1, 0, 0), 3,
//            new BlockPos(1, 0, 1), 4,
//            new BlockPos(0, 0, 1), 5,
//            new BlockPos(-1, 0, 1), 6,
//            new BlockPos(-1, 0, 0), 7,
//            new BlockPos(-1, 0, -1), 8
//    );
//
//    void resetBSide() {
//        currentAdjOrder.clear();
//        currentAdjOrder.addAll(offsetToComparatorSignal.keySet());
//        Collections.shuffle(currentAdjOrder);
//        currentOrderIndex = 0;
//        changeToState(HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT);
//    }
//
//    void changeToState(HydroangeaBsideState newState) {
//        currentState = newState;
//        setChanged();
//        sync();
//
//    }
//
//    void gotoCorrectStateAfterBeingWrong() {
//        if (currentOrderIndex == 0) {
//            changeToState(HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT);
//        } else {
//            changeToState(HydroangeaBsideState.NEEDS_RESET);
//        }
//    }
//
//    boolean isWaterAtOffsetIndex(int indexToCheck) {
//        BlockPos pos = getEffectivePos().offset(currentAdjOrder.get(indexToCheck));
//        FluidState curFluidState = getLevel().getFluidState(pos);
//        TagKey<Fluid> fluidToEat = ((MixinFluidGeneratorBlock) this).getConsumedFluid();
//
//        return curFluidState.is(fluidToEat) && curFluidState.isSource();
//    }
//
////    @Inject(method = "tickFlower", at = @At(value = "HEAD"), cancellable = true, remap = false)
//    private void customTickFlower(CallbackInfo ci) {
//        // We cancel no matter what since the only hydroangea specific code that runs
//        // is related to decay, which we do ourselves in the flower decay mixin.
//        ci.cancel();
//        if (!isBside) {
//            // Do normal hydroangea stuff
//            return;
//        }
//        super.tickFlower();
//
//        if(getLevel().isClientSide) {
//            if (currentState == HydroangeaBsideState.WAITING_FOR_SPOT_TO_DISAPPEAR) {
//                // The setup is done, so show the flower as "ready" with particles
//                for (int i = 0; i < 3; i++) {
//                    WispParticleData data = WispParticleData.wisp((float) Math.random() / 6, 0.1F, 0.1F, 0.1F, 1);
//                    emitParticle(data, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0.5 + Math.random() * 0.2 - 0.1, 0, (float) Math.random() / 30, 0);
//                }
//            }
//            return;
//        }
//
//        if (currentState == HydroangeaBsideState.NEEDS_RESET) {
//            resetBSide();
//        }
//        if (currentState == HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT) {
//            for (int i = 0; i < offsetToComparatorSignal.size(); i++) {
//                if (!isWaterAtOffsetIndex(i)) {
//                    return;
//                }
//            }
//            changeToState(HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT);
//        }
//
//        if (currentState == HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT || currentState == HydroangeaBsideState.WAITING_FOR_SPOT_TO_DISAPPEAR) {
//            // Check that all positions before current pos are not water
//            for (int i = 0; i < currentOrderIndex; i++) {
//                if (isWaterAtOffsetIndex(i)) {
//                    gotoCorrectStateAfterBeingWrong();
//                    return;
//                }
//            }
//            // Check that all positions including and after current pos are water
//            for (int i = currentOrderIndex + 1; i < offsetToComparatorSignal.size(); i++) {
//                if (!isWaterAtOffsetIndex(i)) {
//                    gotoCorrectStateAfterBeingWrong();
//                    return;
//                }
//            }
//        }
//
//        if (currentState == HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT) {
//            if (isWaterAtOffsetIndex(currentOrderIndex)) {
//                changeToState(HydroangeaBsideState.WAITING_FOR_SPOT_TO_DISAPPEAR);
//            } else {
//                gotoCorrectStateAfterBeingWrong();
//            }
//        }
//
//        if (currentState == HydroangeaBsideState.WAITING_FOR_SPOT_TO_DISAPPEAR) {
//            if (!isWaterAtOffsetIndex(currentOrderIndex)) {
//                addMana(STREAK_OUTPUTS.get(currentOrderIndex));
//                setChanged();
//                sync();
//                playSound();
//                currentOrderIndex += 1;
//                if (currentOrderIndex == currentAdjOrder.size()) {
//                    changeToState(HydroangeaBsideState.NEEDS_RESET);
//                } else {
//                    changeToState(HydroangeaBsideState.NEEDS_TO_CHECK_CURRENT_SPOT);
//                }
//            }
//        }
//    }
//
//    public void playSound() {
//        //Usage of vanilla sound event: Subtitle is "Sipping", generic sounds are meant to be reused.
//        getLevel().playSound(null, getEffectivePos(), SoundEvents.GENERIC_DRINK, SoundSource.BLOCKS, 0.31F, 0.5F + (float) Math.random() * 0.5F);
//    }
//
//    @Override
//    public int getMaxMana() {
//        if (isBside) {
//            return STREAK_OUTPUTS.get(STREAK_OUTPUTS.size() - 1);
//        }
//        return 150;
//    }
//
//    @Override
//    public int getComparatorSignal() {
//        if (!isBside) {
//            return 0;
//        }
//
//        if (currentState == HydroangeaBsideState.NEEDS_RESET || currentState == HydroangeaBsideState.WAITING_FOR_INITIAL_WATER_SETUP_TO_BE_CORRECT) {
//            return 0;
//        }
//
//        if (currentAdjOrder == null) {
//            return 0;
//        }
//        return offsetToComparatorSignal.getOrDefault(currentAdjOrder.get(currentOrderIndex), 15);
//    }
//
////    @Inject(method = "writeToPacketNBT", at = @At("RETURN"), remap = false)
//    public void writeToPacketNBT(CompoundTag cmp, CallbackInfo ci) {
//        cmp.putInt(TAG_CUR_ORDER_INDEX, currentOrderIndex);
//        cmp.putString(TAG_STATE_MACHINE_STATE, currentState.name());
//        cmp.putLongArray(TAG_CUR_ADJ_ORDER, currentAdjOrder.stream().map(pos -> pos.asLong()).toList());
//    }
//
////    @Inject(method = "readFromPacketNBT", at = @At("RETURN"), remap = false)
//    public void readFromPacketNBT(CompoundTag cmp, CallbackInfo ci) {
//        currentState = HydroangeaBsideState.valueOf(cmp.getString(TAG_STATE_MACHINE_STATE));
//        currentOrderIndex = cmp.getInt(TAG_CUR_ORDER_INDEX);
//        var positionsAsLongsList = cmp.getLongArray(TAG_CUR_ADJ_ORDER);
//        currentAdjOrder.clear();
//        for (int i = 0; i < positionsAsLongsList.length; i++) {
//            currentAdjOrder.add(BlockPos.of(positionsAsLongsList[i]));
//        }
//    }
//
//    @Override
//    public int getColor() {
//        return 0;
//    }
//
//    @Override
//    public @Nullable RadiusDescriptor getRadius() {
//        return null;
//    }
//}
