package com.forsteri.createmoredrillheads.datagen;

import com.forsteri.createmoredrillheads.CreateMoreDrillHeads;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreateMoreDrillHeads.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TieredDrillDataGen {
    static {
        CreateRegistrate r = CreateMoreDrillHeads.REGISTRATE;
        r.addDataGenerator(ProviderType.RECIPE, TieredDrillSmithingRecipeProvider::provide);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();

        generator.addProvider(true, new DrillTipApplicationRecipeProvider(event.getGenerator().getPackOutput()));
    }
}
