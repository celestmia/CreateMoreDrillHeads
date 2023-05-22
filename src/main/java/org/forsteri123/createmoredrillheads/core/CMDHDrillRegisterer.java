package org.forsteri123.createmoredrillheads.core;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

import java.util.HashMap;
import java.util.Map;

import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static org.forsteri123.createmoredrillheads.core.Registration.REGISTRATE;
import static org.forsteri123.createmoredrillheads.core.Registration.blockPartial;

public class CMDHDrillRegisterer {
    public final PartialModel head;

    public final BlockEntry<CMDHDrillBlock> block;

    public BlockEntityEntry<CMDHDrillTile> tile;

    public static Map<String, BlockEntityEntry<CMDHDrillTile>> tileMap = new HashMap<>();

    public CMDHDrillRegisterer(String name, int breakSpeedMultiplier, TagKey<Block> limit) {
        head = blockPartial(name  + "/head");

        block = REGISTRATE.block(name, (BlockBehaviour.Properties properties) -> new CMDHDrillBlock(properties, breakSpeedMultiplier, name)).
                initialProperties(SharedProperties::stone)
                .properties(p -> p.color(MaterialColor.PODZOL))
                .transform(axeOrPickaxe())
                .blockstate(BlockStateGen.directionalBlockProvider(true))
                .transform(BlockStressDefaults.setImpact(4.0/6.0*breakSpeedMultiplier))
                .onRegister(movementBehaviour(new CMDHBreakingBehavior(breakSpeedMultiplier, limit)))
                .item()
                .transform(customItemModel())
                .register();

        tile = REGISTRATE.blockEntity(
                        name, (BlockEntityType<CMDHDrillTile> type, BlockPos pos, BlockState state) -> new CMDHDrillTile(type, pos, state, breakSpeedMultiplier, limit))
                .instance(() -> (m, tile) -> new CMDHDrillInstance(m, tile, head), false)
                .validBlock(block)
                .register();

        tileMap.put(name, tile);
    }
}
