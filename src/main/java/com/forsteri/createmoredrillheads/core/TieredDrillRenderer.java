package com.forsteri.createmoredrillheads.core;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class TieredDrillRenderer extends DrillRenderer {
    public final PartialModel head;
    public TieredDrillRenderer(BlockEntityRendererProvider.Context context, PartialModel head) {
        super(context);
        this.head = head;
    }

    @Override
    protected SuperByteBuffer getRotatedModel(DrillBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(head, state);
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockState state = context.state;

        SuperByteBuffer superBuffer = CachedBufferer.partial(DrillTierRegisterer.getHead(state), state);
        Direction facing = state.getValue(DrillBlock.FACING);

        float speed = context.contraption.stalled
                || !VecHelper.isVecPointingTowards(context.relativeMotion, facing
                .getOpposite()) ? context.getAnimationSpeed() : 0;
        float time = AnimationTickHolder.getRenderTime() / 20;
        float angle = (time * speed) % 360;

        superBuffer
                .transform(matrices.getModel())
                .centre()
                .rotateY(AngleHelper.horizontalAngle(facing))
                .rotateX(AngleHelper.verticalAngle(facing))
                .rotateZ(angle)
                .unCentre()
                .light(matrices.getWorld(),
                        ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld))
                .renderInto(matrices.getViewProjection(), buffer.getBuffer(RenderType.solid()));
    }
}
