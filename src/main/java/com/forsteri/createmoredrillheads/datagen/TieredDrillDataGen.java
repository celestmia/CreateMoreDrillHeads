package com.forsteri.createmoredrillheads.datagen;

import com.forsteri.createmoredrillheads.CreateMoreDrillHeads;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CreateMoreDrillHeads.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TieredDrillDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();

        generator.addProvider(new DrillTipApplicationRecipeProvider(generator));
    }
}
