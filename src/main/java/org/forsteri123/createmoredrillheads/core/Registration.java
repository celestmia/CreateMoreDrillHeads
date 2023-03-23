package org.forsteri123.createmoredrillheads.core;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.forsteri123.createmoredrillheads.CreateMoreDrillHeads;
import org.jetbrains.annotations.NotNull;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class Registration {

    public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab("createmoredrillheads") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Registration.WOODEN_DRILL.block.get());
        }
    };

    public static final CreateRegistrate REGISTRATE = CreateMoreDrillHeads.registrate()
            .creativeModeTab(() -> CREATIVE_MODE_TAB);

    public static final CMDHDrillRegisterer WOODEN_DRILL = new CMDHDrillRegisterer(
            "wooden_drill", 2, BlockTags.NEEDS_STONE_TOOL
    );

    public static final CMDHDrillRegisterer STONE_DRILL = new CMDHDrillRegisterer(
            "stone_drill", 4, BlockTags.NEEDS_IRON_TOOL
    );

    public static final CMDHDrillRegisterer IRON_DRILL = new CMDHDrillRegisterer(
            "iron_drill", 6, BlockTags.NEEDS_DIAMOND_TOOL
    );

    public static final CMDHDrillRegisterer GOLD_DRILL = new CMDHDrillRegisterer(
            "gold_drill", 12, BlockTags.NEEDS_IRON_TOOL
    );

    public static final CMDHDrillRegisterer DIAMOND_DRILL = new CMDHDrillRegisterer(
            "diamond_drill", 8, null
    );

    public static final CMDHDrillRegisterer NETHERITE_DRILL = new CMDHDrillRegisterer(
            "netherite_drill", 9, null
    );

    public static void register() {}

    protected static PartialModel blockPartial(String path) {
        return new PartialModel(new ResourceLocation(CreateMoreDrillHeads.MOD_ID, ("block/" + path)));
    }
}
