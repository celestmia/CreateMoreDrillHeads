package org.forsteri123.createmoredrillheads.core;

import com.simibubi.create.content.contraptions.components.actors.BlockBreakingKineticTileEntity;
import com.simibubi.create.content.contraptions.components.actors.DrillBlock;
import com.simibubi.create.content.contraptions.components.actors.DrillTileEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CMDHDrillTile extends DrillTileEntity {
    public final int breakSpeedMultiplier;
    public final TagKey<Block> limit;

    public CMDHDrillTile(BlockEntityType<?> typeIn, BlockPos pos, BlockState state, int breakSpeedMultiplier, TagKey<Block> limit) {
        super(typeIn, pos, state);
        this.breakSpeedMultiplier = breakSpeedMultiplier;
        this.limit = limit;
    }

    @Override
    protected float getBreakSpeed() {
        return super.getBreakSpeed() / 6 * breakSpeedMultiplier;
    }

    public boolean canBreak(BlockState stateToBreak, float blockHardness) {
        return
                isBreakable(stateToBreak, blockHardness)
                        && stateToBreak.getTags()
                                .anyMatch((tag) -> tag.equals(BlockTags.MINEABLE_WITH_PICKAXE))
                        && (limit == null || MiningLevelUtil.get(stateToBreak, limit))
                ;
    }
}
