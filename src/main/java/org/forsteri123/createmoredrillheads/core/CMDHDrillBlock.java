package org.forsteri123.createmoredrillheads.core;

import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CMDHDrillBlock extends DrillBlock {

    public final int breakSpeedMultiplier;
    public final String name;
    public CMDHDrillBlock(Properties properties, int breakSpeedMultiplier, String name) {
        super(properties);
        this.breakSpeedMultiplier = breakSpeedMultiplier;
        this.name = name;
    }


    @Override
    public BlockEntityType<? extends DrillBlockEntity> getBlockEntityType() {
        return CMDHDrillRegisterer.tileMap.get(name).get();
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof ItemEntity)
            return;
        if (!new AABB(pos).deflate(.1f)
                .intersects(entityIn.getBoundingBox()))
            return;
        withBlockEntityDo(worldIn, pos, te -> {
            if (te.getSpeed() == 0)
                return;
            entityIn.hurt(damageSourceDrill, (float) getDamage(te.getSpeed()) / 6 * breakSpeedMultiplier);
        });
    }


}
