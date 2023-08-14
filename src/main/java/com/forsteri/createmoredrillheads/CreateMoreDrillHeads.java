package com.forsteri.createmoredrillheads;

import com.forsteri.createmoredrillheads.entry.TieredDrillLang;
import com.forsteri.createmoredrillheads.entry.TieredDrillRegistration;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateMoreDrillHeads.MOD_ID)
public class CreateMoreDrillHeads {

    public static final String MOD_ID = "createmoredrillheads";

    public CreateMoreDrillHeads() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();


        REGISTRATE.registerEventListeners(modEventBus);

        // Register ourselves for server and other game events we are interested in
        TieredDrillRegistration.register();
        TieredDrillLang.register();
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateMoreDrillHeads.MOD_ID);
}
