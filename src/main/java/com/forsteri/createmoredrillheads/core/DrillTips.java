package com.forsteri.createmoredrillheads.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum DrillTips {
    NONE(null, null, () -> ItemStack.EMPTY),
    FORTUNE_I("redstone_dusts", Tags.Items.DUSTS_REDSTONE, () -> {
        ItemStack stack = Items.STICK.getDefaultInstance();
        stack.enchant(Enchantments.BLOCK_FORTUNE, 1);
        return stack;
    }),
    FORTUNE_II("quartz_dusts", ItemTags.create(new ResourceLocation("forge", "dusts/quartz")),
             () -> {
        ItemStack stack = Items.STICK.getDefaultInstance();
        stack.enchant(Enchantments.BLOCK_FORTUNE, 2);
        return stack;
    }),
    FORTUNE_III("emerald_dusts", ItemTags.create(new ResourceLocation("forge", "dusts/emerald")),() -> {
        ItemStack stack = Items.STICK.getDefaultInstance();
        stack.enchant(Enchantments.BLOCK_FORTUNE, 3);
        return stack;
    }),
    SILK_TOUCH("amethyst_dusts", ItemTags.create(new ResourceLocation("forge", "dusts/amethyst")), () -> {
        ItemStack stack = Items.AIR.getDefaultInstance();
        stack.enchant(Enchantments.SILK_TOUCH, 1);
        return stack;
    });
    public String getName() {
        return name;
    }

    public @Nullable TagKey<Item> getMaterial() {
        return material;
    }
    public ItemStack getItemStack() {
        return itemStackSupplier.get();
    }
    private final @Nullable TagKey<Item> material;
    private final String name;
    private final Supplier<ItemStack> itemStackSupplier;
    DrillTips(String name, @Nullable TagKey<Item> material, Supplier<ItemStack> supplier) {
        this.material = material;
        this.name = name;
        this.itemStackSupplier = supplier;
    }
}
