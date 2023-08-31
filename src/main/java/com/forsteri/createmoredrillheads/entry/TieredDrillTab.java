package com.forsteri.createmoredrillheads.entry;

import com.forsteri.createmoredrillheads.CreateMoreDrillHeads;
import com.forsteri.createmoredrillheads.core.DrillTips;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TieredDrillTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER
            = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateMoreDrillHeads.MOD_ID);
    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    public static final RegistryObject<CreativeModeTab> TAB =
            REGISTER.register("more_drill_heads",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.more_drill_heads"))
                            .withTabsBefore(ResourceLocation.of("create:palettes", ':'))
                            .icon(() -> new ItemStack(TieredDrillRegistration.DRILLS.get(Tiers.DIAMOND).DRILLS.get(DrillTips.NONE).getBlock().get()))
                            .displayItems(
                                    (parameters, output) ->
                                            output.acceptAll(
                                                    CreateMoreDrillHeads.REGISTRATE.getAll(Registries.ITEM).stream().map(
                                                            regObj -> new ItemStack(regObj.get())
                                                    ).toList()
                                            )
                            )
                            .build());
}
