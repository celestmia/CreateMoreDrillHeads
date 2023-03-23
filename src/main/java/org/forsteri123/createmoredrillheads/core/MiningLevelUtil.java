package org.forsteri123.createmoredrillheads.core;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MiningLevelUtil {
    public static boolean get(BlockState tags, TagKey<Block> limit) {
        Map<TagKey<Block>, MiningLevel> list = new HashMap<>();
        list.put(BlockTags.NEEDS_STONE_TOOL, MiningLevel.STONE);
        list.put(BlockTags.NEEDS_IRON_TOOL, MiningLevel.IRON);
        list.put(BlockTags.NEEDS_DIAMOND_TOOL, MiningLevel.DIAMOND);

        boolean stone = tags.getTags().noneMatch((tag) -> tag.equals(BlockTags.NEEDS_STONE_TOOL));
        boolean iron = tags.getTags().noneMatch((tag) -> tag.equals(BlockTags.NEEDS_IRON_TOOL));
        boolean diamond = tags.getTags().noneMatch((tag) -> tag.equals(BlockTags.NEEDS_DIAMOND_TOOL));


        return switch (list.get(limit)) {
            case STONE -> stone
                    && iron
                    && diamond;
            case IRON -> iron
                    && diamond;
            case DIAMOND -> diamond;
        };

    }

    static enum MiningLevel {
        STONE,
        IRON,
        DIAMOND
    }
}
