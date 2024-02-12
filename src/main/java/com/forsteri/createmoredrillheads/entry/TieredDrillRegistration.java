package com.forsteri.createmoredrillheads.entry;

import com.forsteri.createmoredrillheads.CreateMoreDrillHeads;
import com.forsteri.createmoredrillheads.core.DrillTierRegisterer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TieredDrillRegistration {
    public static final CreateRegistrate REGISTRATE = CreateMoreDrillHeads.registrate()
            .setCreativeTab(TieredDrillTab.TAB);

    public static final Map<Tier, DrillTierRegisterer> DRILLS = new HashMap<>();

    static {
        for (String material : new String[] {"quartz", "emerald", "amethyst"})
            REGISTRATE.item(material + "_dusts", Item::new)
                    .tag(ItemTags.create(new ResourceLocation("forge", "dusts/" + material)))
                    .register();


        Arrays.stream(Tiers.values()).forEach(
                tier -> {
                    String name = tier.name().toLowerCase() + (
                            tier == Tiers.WOOD || tier == Tiers.GOLD ? "en" : ""
                            );

                    DRILLS.put(
                            tier,
                            new DrillTierRegisterer(
                                    name, tier
                            )
                    );
                }
        );
    }

    public static void register() {}
}
