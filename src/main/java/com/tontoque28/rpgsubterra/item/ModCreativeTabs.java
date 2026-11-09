package com.tontoque28.rpgsubterra.item;

import com.tontoque28.rpgsubterra.RpgSubterra;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeModeTab SUBTERRA_TAB = FabricItemGroupBuilder
            .create(new ResourceLocation(RpgSubterra.MOD_ID, "subterra_tab"))
            .icon(() -> new ItemStack(ModItems.MINER_TIER_3_HELMET))
            .build();

    public static void register() {
    }
}
