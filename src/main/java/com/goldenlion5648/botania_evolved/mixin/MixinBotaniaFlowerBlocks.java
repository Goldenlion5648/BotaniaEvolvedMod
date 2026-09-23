package com.goldenlion5648.botania_evolved.mixin;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.api.block_entity.SpecialFlowerBlockEntity;
import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.function.Supplier;

@Mixin(BotaniaFlowerBlocks.class)
public abstract class MixinBotaniaFlowerBlocks {

    @Redirect(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    ordinal = 2,
                    target = "Lvazkii/botania/common/block/BotaniaFlowerBlocks;createSpecialFlowerBlock(Lnet/minecraft/world/effect/MobEffect;ILnet/minecraft/world/level/block/state/BlockBehaviour$Properties;Ljava/util/function/Supplier;)Lnet/minecraft/world/level/block/FlowerBlock;"
            ),
            remap = false
    )
    private static FlowerBlock modifyHydroangeas(
            MobEffect effect,
            int duration,
            BlockBehaviour.Properties props,
            Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType
    ) {
        return invokeCreateSpecialFlowerBlock(
                effect,
                duration,
                props,
                beType,
                true
        );
    }

    @Invoker("createSpecialFlowerBlock")
    protected static FlowerBlock invokeCreateSpecialFlowerBlock(
            MobEffect effect,
            int effectDuration,
            BlockBehaviour.Properties props,
            Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType,
            boolean hasComparatorOutput
    ) {
        throw new AssertionError();
    }


}