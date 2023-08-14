package com.forsteri.createmoredrillheads.core;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.kinetics.drill.DrillMovementBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.TierSortingRegistry;

import javax.annotation.Nullable;

public class TieredDrillBreakingBehavior extends DrillMovementBehaviour {
    public final Tier tier;
    public final DrillTips tip;

    public TieredDrillBreakingBehavior(Tier tier, DrillTips tip) {
        super();
        this.tip = tip;
        this.tier = tier;
    }

    @Override
    @OnlyIn(value = Dist.CLIENT)
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
        ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (!ContraptionRenderDispatcher.canInstance())
            TieredDrillRenderer.renderInContraption(context, renderWorld, matrices, buffer);
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new TieredDrillActorInstance(materialManager, simulationWorld, context);
    }

    @Override
    protected void destroyBlock(MovementContext context, BlockPos breakingPos) {
        BlockHelper.destroyBlockAs(context.world, breakingPos, null, tip.getItemStack(), 1f, stack -> this.dropItem(context, stack));
    }

    @Override
    public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
        return super.canBreak(world, breakingPos, state)
                && TierSortingRegistry.isCorrectTierForDrops(tier, state);
    }

    @Override
    protected float getBlockBreakingSpeed(MovementContext context) {
        return super.getBlockBreakingSpeed(context) / 6f * tier.getSpeed();
    }
}
