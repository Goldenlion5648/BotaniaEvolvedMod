package com.goldenlion5648.botania_evolved.mixin.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.flower.generating.FluidGeneratorBlockEntity;

@Mixin(FluidGeneratorBlockEntity.class)
public abstract class MixinFluidGeneratorBlock extends GeneratingFlowerBlockEntity {

    public MixinFluidGeneratorBlock(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
//
//    @Inject(method = "tickFlower", at = @At("HEAD"), cancellable = true, remap = false)
//    private void bSideCheckBeforeTick(CallbackInfo ci) {
//        if (((IBSideFlower) (Object) this).isBside()) {
//            ci.cancel();
//        }
//    }
}
