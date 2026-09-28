package com.goldenlion5648.botania_evolved;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = BotaniaEvolved.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue ENDOFLAME_TICKS_BEFORE_DECAY = BUILDER
            .comment("Ticks before the endoflame decays")
            .defineInRange("ENDOFLAME_TICKS_BEFORE_DECAY", 80, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .define("items", List.of("minecraft:iron_ingot"), Config::validateItemName);
//
//    static {
//        HashMap<String, Integer> defaultFlowerToTicks = new HashMap<>();
//        for (var x : BuiltInRegistries.ITEM.getTagOrEmpty(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS)) {
//            defaultFlowerToTicks.put(x.ge, 50);
//        }
//        final ForgeConfigSpec.ConfigValue<HashMap<String, Integer>> FLOWER_TO_TICKS = BUILDER
//            .comment("Flower to ticks before decay.")
//            .define("FLOWER_TO_TICKS", defaultFlowerToTicks);
//    }

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

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
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get();

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
                .map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName)))
                .collect(Collectors.toSet());
    }
}
