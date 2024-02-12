package com.forsteri.createmoredrillheads.core;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;

public class RendererLoader {
    public static void addRenderer
            (
                    BlockEntityBuilder<TieredDrillBlockEntity, CreateRegistrate> entity,
                    PartialModel head
            ) {
        entity.renderer(() -> context -> new TieredDrillRenderer(context, head));
    }

}
