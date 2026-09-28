package com.goldenlion5648.botania_evolved.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntity;
import vazkii.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;

import java.util.List;

@Mixin(TerrestrialAgglomerationPlateBlockEntity.class)
public abstract class MixinAgglom extends BotaniaBlockEntity implements SparkAttachable, ManaReceiver {
    public MixinAgglom(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Shadow
    private List<ItemEntity> getItemEntities() {
        // dummy inside
        return null;
    }

    @Overwrite(remap = false)
    private boolean hasValidPlatform() {
//        List<ItemEntity> itemEntities = getItemEntities();
//        List<ItemStack> items = getItems(itemEntities);
//        SimpleContainer inv = getInventory(itemEntities);
        return level.getBlockState(getBlockPos().below()).getBlock() == Blocks.IRON_BLOCK;
    }
}
