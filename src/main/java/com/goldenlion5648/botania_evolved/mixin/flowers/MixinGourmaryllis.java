package com.goldenlion5648.botania_evolved.mixin.flowers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.flower.generating.GourmaryllisBlockEntity;

@Mixin(GourmaryllisBlockEntity.class)
public abstract class MixinGourmaryllis extends GeneratingFlowerBlockEntity {

    public MixinGourmaryllis(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(method = "tickFlower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    public Item getItemForEating(ItemStack instance) {
        CompoundTag tag = instance.getTag();
        if (instance.getItem() == Items.SUSPICIOUS_STEW && tag != null && tag.contains("Effects")) {
            return Items.COOKED_BEEF;
        }
        return Items.COBBLESTONE;
    }

    /*
    {
        Effects: [
            {
                EffectDuration: 200,
                EffectId: 24,
                "forge:effect_id": "minecraft:glowing"
     */
    @Overwrite(remap = false)
    private static int getFoodValue(ItemStack stack) {
        CompoundTag effectTag = stack.getTag();
        ListTag effectsList = ((ListTag) (stack.getTag().get("Effects")));
        if (effectsList.size() == 0) {
            return 0;
        }
        CompoundTag firstEffect = effectsList.getCompound(0);
        int effectDuration = firstEffect.getInt("EffectDuration");
        int effectId = firstEffect.getInt("EffectId");
        // Caps at 3:00
        int reduced_duration = (Math.min(180 * 20, effectDuration) / 30) + 1;
        return reduced_duration * reduced_duration;
    }
}
