package com.forsteri.createmoredrillheads.core;

import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TieredDrillBlock extends DrillBlock {

    public final Tier tier;
    public final String name;
    private final Supplier<BlockEntityEntry<? extends DrillBlockEntity>> blockEntityTypeSupplier;
    public TieredDrillBlock(Properties properties, Tier tier, String name, Supplier<BlockEntityEntry<? extends DrillBlockEntity>> blockEntityTypeSupplier) {
        super(properties);
        this.tier = tier;
        this.name = name;
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }


    @Override
    public @NotNull BlockEntityType<? extends DrillBlockEntity> getBlockEntityType() {
        return blockEntityTypeSupplier.get().get();
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Entity entityIn) {
        if (entityIn instanceof ItemEntity)
            return;
        if (!new AABB(pos).deflate(.1f)
                .intersects(entityIn.getBoundingBox()))
            return;
        withBlockEntityDo(worldIn, pos, te -> {
            if (te.getSpeed() == 0)
                return;
            entityIn.hurt(CreateDamageSources.drill(worldIn), (float) getDamage(te.getSpeed()) / 6 * tier.getSpeed());
        });
    }


}
