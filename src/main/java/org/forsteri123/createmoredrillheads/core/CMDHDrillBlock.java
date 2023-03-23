package org.forsteri123.createmoredrillheads.core;

import com.simibubi.create.content.contraptions.components.actors.DrillBlock;
import com.simibubi.create.content.contraptions.components.actors.DrillTileEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Supplier;

public class CMDHDrillBlock extends DrillBlock {

    public final int breakSpeedMultiplier;
    public final String name;
    public CMDHDrillBlock(Properties properties, int breakSpeedMultiplier, String name) {
        super(properties);
        this.breakSpeedMultiplier = breakSpeedMultiplier;
        this.name = name;
    }


    @Override
    public BlockEntityType<? extends DrillTileEntity> getTileEntityType() {
        return CMDHDrillRegisterer.tileMap.get(name).get();
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof ItemEntity)
            return;
        if (!new AABB(pos).deflate(.1f)
                .intersects(entityIn.getBoundingBox()))
            return;
        withTileEntityDo(worldIn, pos, te -> {
            if (te.getSpeed() == 0)
                return;
            entityIn.hurt(damageSourceDrill, (float) getDamage(te.getSpeed()) / 6 * breakSpeedMultiplier);
        });
    }


}
