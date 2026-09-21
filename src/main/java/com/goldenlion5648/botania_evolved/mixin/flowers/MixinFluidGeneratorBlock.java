package com.goldenlion5648.botania_evolved.mixin.flowers;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.common.block.flower.generating.FluidGeneratorBlockEntity;

@Mixin(FluidGeneratorBlockEntity.class)
public interface MixinFluidGeneratorBlock {
    @Accessor("consumedFluid")
    TagKey<Fluid> getConsumedFluid();
}
