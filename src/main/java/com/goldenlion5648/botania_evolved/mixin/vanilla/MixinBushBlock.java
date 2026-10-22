package com.goldenlion5648.botania_evolved.mixin.vanilla;

import com.goldenlion5648.botania_evolved.helpers.BotaniaEvolvedTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.common.lib.BotaniaTags;

@Mixin(BushBlock.class)
public abstract class MixinBushBlock extends Block implements net.minecraftforge.common.IPlantable {

    public MixinBushBlock(Properties pProperties) {
        super(pProperties);
    }

    @Inject(method = "mayPlaceOn", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private void mayPlaceOn(
            BlockState pState,
            BlockGetter pLevel,
            BlockPos pPos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (this.defaultBlockState().is(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS)
                && pState.is(BotaniaEvolvedTags.Blocks.B_SIDE_SOIL)) {
            cir.setReturnValue(true);
        }
    }
    //    @Overwrite
//    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
//        if (this.defaultBlockState().is(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS) && (pState.is(BotaniaEvolvedTags.Blocks.B_SIDE_SOIL))) {
//            return true;
//        }
//        return pState.is(BlockTags.DIRT) || pState.is(net.minecraft.world.level.block.Blocks.FARMLAND);
//    }
}