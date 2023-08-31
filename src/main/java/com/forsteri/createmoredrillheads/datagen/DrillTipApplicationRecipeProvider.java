package com.forsteri.createmoredrillheads.datagen;

import com.forsteri.createmoredrillheads.CreateMoreDrillHeads;
import com.forsteri.createmoredrillheads.core.DrillTips;
import com.forsteri.createmoredrillheads.core.TippedDrillRegisterer;
import com.forsteri.createmoredrillheads.entry.TieredDrillRegistration;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.ProcessingRecipeGen;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;

import java.util.Map;
import java.util.function.UnaryOperator;

public class DrillTipApplicationRecipeProvider extends ProcessingRecipeGen {
    public DrillTipApplicationRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.DEPLOYING;
    }

    {
        for (Tiers tier : Tiers.values()) {
            for (DrillTips tip : DrillTips.values()) {
                if (tip == DrillTips.NONE)
                    continue;

                Map<DrillTips, TippedDrillRegisterer> tippedDrillRegistererMap = TieredDrillRegistration.DRILLS.get(tier).DRILLS;

                create(tier.name().toLowerCase() + "_drill_" + tip.getName() + "_tip_attaching_recipe",
                        b -> b.require(tippedDrillRegistererMap.get(DrillTips.NONE).getBlock().get())
                                .require(tip.getMaterial())
                                .output(tippedDrillRegistererMap.get(tip).getBlock().get())
                        );
            }
        }
    }

    <T extends ProcessingRecipe<?>> void create(String name,
                                                UnaryOperator<ProcessingRecipeBuilder<T>> transform) {
        create(new ResourceLocation(CreateMoreDrillHeads.MOD_ID, name), transform);
    }
}
