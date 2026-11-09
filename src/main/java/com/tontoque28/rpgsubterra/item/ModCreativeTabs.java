package com.tontoque28.rpgsubterra.item;

import com.tontoque28.rpgsubterra.RpgSubterra;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeModeTab SUBTERRA_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(RpgSubterra.MOD_ID, "subterra_tab"),
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(ModItems.MINER_TIER_3_HELMET))
                    .title(Component.translatable("creativetab.rpgsubterra.subterra_tab"))
                    .displayItems((parameters, output) -> {
                        for (Item item : ModItems.all()) {
                            output.accept(item);
                        }
                    })
                    .build());

    public static void register() {
    }
}
