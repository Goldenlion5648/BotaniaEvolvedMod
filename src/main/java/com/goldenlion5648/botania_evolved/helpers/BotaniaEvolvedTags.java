package com.goldenlion5648.botania_evolved.helpers;

import com.goldenlion5648.botania_evolved.BotaniaEvolved;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BotaniaEvolvedTags {
    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(BotaniaEvolved.MOD_ID, name));
        }
        public static final TagKey<Block> B_SIDE_SOIL = tag("b_side_soil");
    }
}
