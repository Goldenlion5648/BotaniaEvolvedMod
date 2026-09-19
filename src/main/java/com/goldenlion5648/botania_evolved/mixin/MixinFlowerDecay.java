package com.goldenlion5648.botania_evolved.mixin;

import net.minecraft.core.BlockPos;
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

    private int ticksLived = 0;
    private int dieAfterXTicks = 60;

    public MixinFlowerDecay(BlockEntityType<?> type, BlockPos pos, BlockState state, Class<ManaCollector> bindClass) {
        super(type, pos, state, bindClass);
    }

    @Inject(method = "tickFlower", at = @At("HEAD"), remap = false)
    void checkDecay(CallbackInfo ci) {
        if (!getLevel().isClientSide) {
            if (ticksLived++ > dieAfterXTicks) {
                getLevel().destroyBlock(getBlockPos(), false);
                if (Blocks.DEAD_BUSH.defaultBlockState().canSurvive(getLevel(), getBlockPos())) {
                    getLevel().setBlockAndUpdate(getBlockPos(), Blocks.DEAD_BUSH.defaultBlockState());
                }
            }
        }
    }
}
