package com.forsteri.createmoredrillheads.core;

import com.forsteri.createmoredrillheads.CreateMoreDrillHeads;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.fml.DistExecutor;

import static com.forsteri.createmoredrillheads.entry.TieredDrillRegistration.REGISTRATE;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class TippedDrillRegisterer {
    public PartialModel getHead() {
        return head;
    }

    public BlockEntry<TieredDrillBlock> getBlock() {
        return block;
    }

    public BlockEntityEntry<TieredDrillBlockEntity> getTile() {
        return tile;
    }

    private final PartialModel head;

    private final BlockEntry<TieredDrillBlock> block;

    private final BlockEntityEntry<TieredDrillBlockEntity> tile;

    public TippedDrillRegisterer(String name, Tiers tier, DrillTips tip) {
        head = new PartialModel(new ResourceLocation(CreateMoreDrillHeads.MOD_ID, "block/" + name + "/head"));

        block = REGISTRATE.block(name, (BlockBehaviour.Properties properties) ->
                        new TieredDrillBlock(properties, tier, name, this::getTile))
                .initialProperties(SharedProperties::stone)
                .properties(p -> p.color(MaterialColor.PODZOL))
                .transform(axeOrPickaxe())
                .blockstate(
                        (c, p) -> p.directionalBlock(c.get(), $ -> p.models()
                                .getExistingFile(new ResourceLocation("createmoredrillheads", "block/abstract/block")))
                )
                .transform(BlockStressDefaults.setImpact(4.0 / 6.0 * tier.getSpeed()))
                .onRegister(movementBehaviour(new TieredDrillBreakingBehavior(tier, tip)))
                .item()
                .tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
                .model((c, p) -> {
                    ResourceLocation itemParent, headParent;
                    if (tip == DrillTips.NONE) {
                        itemParent = new ResourceLocation("createmoredrillheads", "block/abstract/abstract_tiered_drill_item");
                        headParent = new ResourceLocation("createmoredrillheads", "block/abstract/head");
                    } else {
                        itemParent = new ResourceLocation("createmoredrillheads", "block/abstract/abstract_tipped_tiered_drill_item");
                        headParent = new ResourceLocation("createmoredrillheads", "block/abstract/abstract_tipped_tiered_drill_head");
                    }

                    buildModel(p.withExistingParent(c.getName(), itemParent), tier, tip);
                    buildModel(p.withExistingParent("block/" + c.getName() + "/head", headParent), tier, tip);
                })
                .build()
                .register();

        var unregistered = REGISTRATE.blockEntity(
                        name, (BlockEntityType<TieredDrillBlockEntity> type, BlockPos pos, BlockState state) -> new TieredDrillBlockEntity(type, pos, state, tier, tip))
                .instance(() -> (m, tile) -> new TieredDrillInstance(m, tile, head), false)
                .validBlock(block);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> RendererLoader.addRenderer(unregistered, head));

        tile = unregistered.register();
    }

    private <U extends ModelBuilder<U>> void buildModel(U builder, Tiers tier, DrillTips tip) {
        String value1;
        String value2;
        switch (tier) {
            case WOOD -> {
                value1 = "block/oak_log";
                value2 = "block/oak_planks";
            }
            case STONE -> {
                value1 = "block/stone";
                value2 = "block/cobblestone";
            }
            case IRON -> {
                value1 = "block/iron_block";
                value2 = "block/iron_block";
            }
            case DIAMOND -> {
                value1 = "block/diamond_block";
                value2 = "block/diamond_block";
            }
            case GOLD -> {
                value1 = "block/gold_block";
                value2 = "block/gold_block";
            }
            case NETHERITE -> {
                value1 = "block/netherite_block";
                value2 = "block/netherite_block";
            }
            default -> throw new IllegalStateException("Unexpected value: " + tier);
        }

        value1 = "minecraft:" + value1;
        value2 = "minecraft:" + value2;

        builder.texture("2", value1);
        builder.texture("7", value2);
        builder.texture("10", value2);

        if (tip != DrillTips.NONE)
            builder.texture("dust", switch (tip) {
                case FORTUNE_I -> "minecraft:item/redstone";
                case FORTUNE_II -> "createmoredrillheads:item/quartz_dusts";
                case FORTUNE_III -> "createmoredrillheads:item/emerald_dusts";
                case SILK_TOUCH -> "createmoredrillheads:item/amethyst_dusts";
                default -> throw new IllegalStateException("Unexpected value: " + tip);
            });
    }

}
