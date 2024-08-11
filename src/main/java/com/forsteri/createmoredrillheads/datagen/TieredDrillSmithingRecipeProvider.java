package com.forsteri.createmoredrillheads.datagen;

import com.forsteri.createmoredrillheads.core.DrillTierRegisterer;
import com.forsteri.createmoredrillheads.core.DrillTips;
import com.forsteri.createmoredrillheads.core.TippedDrillRegisterer;
import com.forsteri.createmoredrillheads.entry.TieredDrillRegistration;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

public class TieredDrillSmithingRecipeProvider {
    public static void provide(RegistrateRecipeProvider p) {
        provideDiamondToNetheriteDrills(p);
    }

    private static void provideDiamondToNetheriteDrills(RegistrateRecipeProvider p) {
        DrillTierRegisterer diamondDrillRegisterer = TieredDrillRegistration.DRILLS.get(Tiers.DIAMOND);
        DrillTierRegisterer netheriteDrillRegisterer = TieredDrillRegistration.DRILLS.get(Tiers.NETHERITE);

        for (Map.Entry<DrillTips, TippedDrillRegisterer> entry : diamondDrillRegisterer.DRILLS.entrySet()) {
            DrillTips tip = entry.getKey();
            TippedDrillRegisterer diamondDrill = entry.getValue();
            TippedDrillRegisterer netheriteDrill = netheriteDrillRegisterer.DRILLS.get(tip);
            netheriteSmithing(p, diamondDrill.getBlock().asItem(), RecipeCategory.MISC, netheriteDrill.getBlock().asItem());
        }
    }

    private static void netheriteSmithing(RegistrateRecipeProvider p, Item base, RecipeCategory category, Item result) {
        SmithingTransformRecipeBuilder
                .smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(base),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        category,
                        result)
                .unlocks("has_netherite_ingot", RegistrateRecipeProvider.has(Items.NETHERITE_INGOT))
                .save(p, p.safeId(result) + "_from_" + p.safeName(base) + "_smithing");
    }
}
