package com.goldenlion5648.botania_evolved.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class ModLootTableProvider extends LootTableProvider  {
    public ModLootTableProvider(PackOutput output) {
        super(
                output,
                Collections.emptySet(),
                List.of(
                        new LootTableProvider.SubProviderEntry(
                                FlowerLootProvider::new,
                                LootContextParamSets.BLOCK
                        )
                )
        );
    }
    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                new ModLootTableProvider(event.getGenerator().getPackOutput())
        );
    }
}
