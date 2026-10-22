package com.goldenlion5648.botania_evolved;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = BotaniaEvolved.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue TICKS_BEFORE_DECAY = BUILDER
            .comment("Number of ticks before passive flowers decay. Default is 1 hour real time. Set to -1 to never decay.")
            .defineInRange("TICKS_BEFORE_DECAY", 72000, -1, Integer.MAX_VALUE);

    static final ForgeConfigSpec SPEC = BUILDER.build();
    public static int ticksBeforeDecayValue;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

//    private static boolean validateFlowerName(final Object obj)
//    {
//        return obj instanceof final String itemName && BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS;
//    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        ticksBeforeDecayValue = TICKS_BEFORE_DECAY.get();

    }
}
