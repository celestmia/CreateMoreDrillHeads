package org.forsteri123.createmoredrillheads.core;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorInstance;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.kinetics.base.BlockBreakingMovementBehaviour;
import com.simibubi.create.content.kinetics.drill.DrillActorInstance;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillRenderer;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class CMDHBreakingBehavior extends BlockBreakingMovementBehaviour {
    public final int breakSpeedMultiplier;
    public final TagKey<Block> limit;

    public CMDHBreakingBehavior(int breakSpeedMultiplier, TagKey<Block> limit) {
        super();
        this.breakSpeedMultiplier = breakSpeedMultiplier;
        this.limit = limit;
    }


    @Override
    public void damageEntities(MovementContext context, BlockPos pos, Level world) {
        if (context.contraption.entity instanceof OrientedContraptionEntity oce && oce.nonDamageTicks > 0)
            return;
        DamageSource damageSource = getDamageSource();
        if (damageSource == null && !throwsEntities())
            return;
        Entities: for (Entity entity : world.getEntitiesOfClass(Entity.class, new AABB(pos))) {
            if (entity instanceof ItemEntity)
                continue;
            if (entity instanceof AbstractContraptionEntity)
                continue;
            if (entity.isPassengerOfSameVehicle(context.contraption.entity))
                continue;
            if (entity instanceof AbstractMinecart)
                for (Entity passenger : entity.getIndirectPassengers())
                    if (passenger instanceof AbstractContraptionEntity
                            && ((AbstractContraptionEntity) passenger).getContraption() == context.contraption)
                        continue Entities;

            if (damageSource != null && !world.isClientSide) {
                float damage = (float) Mth.clamp(breakSpeedMultiplier * Math.pow(context.relativeMotion.length(), 0.4) + 1, 2, 10);
                entity.hurt(damageSource, damage);
            }
            if (throwsEntities() && (world.isClientSide == (entity instanceof Player)))
                throwEntity(context, entity);
        }
    }

    @Override
    public void tickBreaker(MovementContext context) {
        CompoundTag data = context.data;
        if (context.world.isClientSide)
            return;
        if (!data.contains("BreakingPos")) {
            context.stall = false;
            return;
        }
        if (context.relativeMotion.equals(Vec3.ZERO)) {
            context.stall = false;
            return;
        }

        int ticksUntilNextProgress = data.getInt("TicksUntilNextProgress");
        if (ticksUntilNextProgress-- > 0) {
            data.putInt("TicksUntilNextProgress", ticksUntilNextProgress);
            return;
        }

        Level world = context.world;
        BlockPos breakingPos = NbtUtils.readBlockPos(data.getCompound("BreakingPos"));
        int destroyProgress = data.getInt("Progress");
        int id = data.getInt("BreakerId");
        BlockState stateToBreak = world.getBlockState(breakingPos);
        float blockHardness = stateToBreak.getDestroySpeed(world, breakingPos);

        if (!canBreak(world, breakingPos, stateToBreak)) {
            if (destroyProgress != 0) {
                destroyProgress = 0;
                data.remove("Progress");
                data.remove("TicksUntilNextProgress");
                data.remove("BreakingPos");
                world.destroyBlockProgress(id, breakingPos, -1);
            }
            context.stall = false;
            return;
        }

        float breakSpeed = Mth.clamp(Math.abs(context.getAnimationSpeed()) / 500f / 6 * breakSpeedMultiplier, 1 / 128f / 6 * breakSpeedMultiplier, 16f / 6 * breakSpeedMultiplier);
        destroyProgress += Mth.clamp((int) (breakSpeed / blockHardness), 1, 10 - destroyProgress);
        world.playSound(null, breakingPos, stateToBreak.getSoundType()
                .getHitSound(), SoundSource.NEUTRAL, .25f, 1);

        if (destroyProgress >= 10) {
            world.destroyBlockProgress(id, breakingPos, -1);

            // break falling blocks from top to bottom
            BlockPos ogPos = breakingPos;
            BlockState stateAbove = world.getBlockState(breakingPos.above());
            while (stateAbove.getBlock() instanceof FallingBlock) {
                breakingPos = breakingPos.above();
                stateAbove = world.getBlockState(breakingPos.above());
            }
            stateToBreak = world.getBlockState(breakingPos);

            context.stall = false;
            if (shouldDestroyStartBlock(stateToBreak))
                BlockHelper.destroyBlock(context.world, breakingPos, 1f, stack -> this.dropItem(context, stack));
            onBlockBroken(context, ogPos, stateToBreak);
            ticksUntilNextProgress = -1;
            data.remove("Progress");
            data.remove("TicksUntilNextProgress");
            data.remove("BreakingPos");
            return;
        }

        ticksUntilNextProgress = (int) (blockHardness / breakSpeed);
        world.destroyBlockProgress(id, breakingPos, (int) destroyProgress);
        data.putInt("TicksUntilNextProgress", ticksUntilNextProgress);
        data.putInt("Progress", destroyProgress);
    }

    @Override
    public boolean isActive(MovementContext context) {
        return !VecHelper.isVecPointingTowards(context.relativeMotion, context.state.getValue(DrillBlock.FACING)
                .getOpposite());
    }

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(context.state.getValue(DrillBlock.FACING)
                .getNormal()).scale(.65f);
    }

    @Override
    @OnlyIn(value = Dist.CLIENT)
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                    ContraptionMatrices matrices, MultiBufferSource buffer) {
        if (!ContraptionRenderDispatcher.canInstance())
            DrillRenderer.renderInContraption(context, renderWorld, matrices, buffer);
    }

    @Override
    public boolean hasSpecialInstancedRendering() {
        return true;
    }

    @Nullable
    @Override
    public ActorInstance createInstance(MaterialManager materialManager, VirtualRenderWorld simulationWorld, MovementContext context) {
        return new DrillActorInstance(materialManager, simulationWorld, context);
    }

    @Override
    protected DamageSource getDamageSource() {
        return DrillBlock.damageSourceDrill;
    }

    @Override
    public boolean canBreak(Level world, BlockPos breakingPos, BlockState state) {
        return super.canBreak(world, breakingPos, state) && !state.getCollisionShape(world, breakingPos)
                .isEmpty() && !AllBlocks.TRACK.has(state) && state.getTags()
                .anyMatch((tag) -> tag.equals(BlockTags.MINEABLE_WITH_PICKAXE))
                && (limit == null || MiningLevelUtil.get(state, limit));
    }
}
