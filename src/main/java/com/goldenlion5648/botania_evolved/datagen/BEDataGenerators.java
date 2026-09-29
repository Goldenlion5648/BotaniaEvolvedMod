package com.goldenlion5648.botania_evolved.datagen;

import com.goldenlion5648.botania_evolved.BotaniaEvolved;
import com.goldenlion5648.botania_evolved.helpers.BEBlockTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(
        modid = BotaniaEvolved.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class BEDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        CompletableFuture<HolderLookup.Provider> lookupProvider =
                event.getLookupProvider();

        ExistingFileHelper existingFileHelper =
                event.getExistingFileHelper();

        generator.addProvider(
                event.includeServer(),
                new BEBlockTagProvider(
                        output,
                        lookupProvider,
                        BotaniaEvolved.MOD_ID,
                        existingFileHelper
                )
        );
    }
}
