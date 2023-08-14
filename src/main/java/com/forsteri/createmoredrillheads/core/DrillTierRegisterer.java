package com.forsteri.createmoredrillheads.core;

import com.forsteri.createmoredrillheads.entry.TieredDrillRegistration;
import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DrillTierRegisterer {
    public Map<DrillTips, TippedDrillRegisterer> DRILLS = new HashMap<>();

    public DrillTierRegisterer(String name, Tiers tier) {
        for (DrillTips tip : DrillTips.values()) {
            String prefix = tip == DrillTips.NONE ? "" : tip.getName().toLowerCase() + "_tipped_";
            initDrill(
                    tip,
                    prefix + name + "_drill",
                    tier
            );
        }
    }

    private void initDrill(DrillTips tip, String name, Tiers tier) {
        DRILLS.put(tip,
            new TippedDrillRegisterer(
                    name,
                    tier,
                    tip
            )
        );
    }

    public static PartialModel getHead(BlockState state) {
        Optional<DrillTierRegisterer> registerer =
                TieredDrillRegistration.DRILLS.values().stream()
                        .filter(drillTierRegisterer -> drillTierRegisterer.DRILLS.values().stream().anyMatch(
                                tippedDrillRegisterer -> tippedDrillRegisterer.getBlock().get().equals(state.getBlock()))
                        ).findFirst();

        if (registerer.isEmpty())
            throw new IllegalStateException("No drill tier registerer found for block " + state.getBlock().getRegistryName());

        Optional<TippedDrillRegisterer> tippedDrillRegisterer = registerer.get().DRILLS.values().stream()
                .filter(drillRegisterer -> drillRegisterer.getBlock().get().equals(state.getBlock()))
                .findFirst();

        if (tippedDrillRegisterer.isEmpty())
            throw new IllegalStateException("No tipped drill registerer found for block " + state.getBlock().getRegistryName());

        return tippedDrillRegisterer.get().getHead();
    }
}
