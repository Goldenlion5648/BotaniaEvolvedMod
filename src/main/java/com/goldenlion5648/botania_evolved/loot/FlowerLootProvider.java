package com.goldenlion5648.botania_evolved.loot;

import com.goldenlion5648.botania_evolved.mixin.MixinFlowerDecay;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.Collections;

public class FlowerLootProvider extends BlockLootSubProvider {

    public FlowerLootProvider() {
        super(
                Collections.emptySet(),
                FeatureFlags.REGISTRY.allFlags()
        );
    }

    protected static LootTable.Builder genCopyNbt(Block b, String... tags) {
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b);
        CopyNbtFunction.Builder func = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);
        for (String tag : tags) {
            func = func.copy(tag, "BlockEntityTag." + tag);
        }
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
                .when(ExplosionCondition.survivesExplosion())
                .apply(func);
        return LootTable.lootTable().withPool(pool);
    }


    @Override
    protected void generate() {
        for(var generatingFlower : BuiltInRegistries.BLOCK.getTagOrEmpty(BotaniaTags.Blocks.GENERATING_SPECIAL_FLOWERS)) {
            add(generatingFlower.get(), genCopyNbt(generatingFlower.get(), MixinFlowerDecay.TAG_TICKS_EXISTED));
        }

    }

}
