package com.goldenlion5648.botania_evolved.mixin.flowers;

import com.goldenlion5648.botania_evolved.api.IBSideFlower;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.flower.generating.GourmaryllisBlockEntity;

@Mixin(GourmaryllisBlockEntity.class)
public abstract class MixinGourmaryllis extends GeneratingFlowerBlockEntity {

    public MixinGourmaryllis(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    boolean checkIsBside() {
        return ((IBSideFlower) this).isBside();
    }

    @WrapOperation(method = "tickFlower", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"))
    public Item getItemForEating(ItemStack instance, Operation<Item> original) {
        if (!checkIsBside()) {
            return original.call(instance);
        }
        CompoundTag tag = instance.getTag();
        if (tag != null && original.call(instance) == Items.SUSPICIOUS_STEW && tag.contains("Effects")) {
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
    @WrapOperation(
            method = "tickFlower",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/botania/common/block/flower/generating/GourmaryllisBlockEntity;getFoodValue(Lnet/minecraft/world/item/ItemStack;)I"
            ),
            remap = false
    )
    private int wrapGetFoodValue(
            ItemStack stack,
            Operation<Integer> original
    ) {
        if (!checkIsBside()) {
            return original.call(stack);
        }

        CompoundTag effectTag = stack.getTag();
        if (effectTag == null) {
            return 0;
        }
        ListTag effectsList = ((ListTag) (stack.getTag().get("Effects")));
        if (effectsList.size() == 0) {
            return 0;
        }
        CompoundTag firstEffect = effectsList.getCompound(0);
        int effectDuration = firstEffect.getInt("EffectDuration");
        int effectId = firstEffect.getInt("EffectId");
        // Caps at 3:00
        int reduced_duration = (Math.min(180 * 20, effectDuration) / 30) + 1;
        if (reduced_duration >= 10) {
            reduced_duration = 10 + reduced_duration / 10;
        }
        return reduced_duration;
    }

}
