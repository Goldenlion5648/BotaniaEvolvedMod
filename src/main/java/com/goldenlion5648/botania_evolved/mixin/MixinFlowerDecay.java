package com.goldenlion5648.botania_evolved.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.api.mana.ManaCollector;

@Mixin(GeneratingFlowerBlockEntity.class)
public abstract class MixinFlowerDecay extends BindableSpecialFlowerBlockEntity<ManaCollector> {

    private int dieAfterXTicks = 60000;
//    private int ticksLivedForDecay = 0;
//    private static final String TAG_TICKS_LIVED_FOR_DECAY = "ticksLivedForDecay";


    public MixinFlowerDecay(BlockEntityType<?> type, BlockPos pos, BlockState state, Class<ManaCollector> bindClass) {
        super(type, pos, state, bindClass);
    }

    @Inject(method = "readFromPacketNBT", at = @At("RETURN"), remap = false)
    public void readDecayTicks(CompoundTag cmp, CallbackInfo ci) {
        ticksExisted = cmp.getInt(TAG_TICKS_EXISTED);
    }

    @Inject(method = "writeToPacketNBT", at = @At("RETURN"), remap = false)
    public void writeDecayTicks(CompoundTag cmp, CallbackInfo ci) {
        cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
    }

    @Inject(method = "tickFlower", at = @At("HEAD"), remap = false)
    void checkDecay(CallbackInfo ci) {
        if (!getLevel().isClientSide) {
            if (ticksExisted > dieAfterXTicks) {
                getLevel().destroyBlock(getBlockPos(), false);
                if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
                    getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
                }
            }
        }
    }
}
