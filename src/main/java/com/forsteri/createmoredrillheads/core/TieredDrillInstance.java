package com.forsteri.createmoredrillheads.core;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class TieredDrillInstance extends SingleRotatingInstance<TieredDrillBlockEntity> {
    public final PartialModel head;

    public TieredDrillInstance(MaterialManager modelManager, TieredDrillBlockEntity tile, PartialModel head) {
        super(modelManager, tile);
        this.head = head;
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = referenceState.getValue(BlockStateProperties.FACING);
        return getRotatingMaterial().getModel(head, referenceState, facing);
    }
}
