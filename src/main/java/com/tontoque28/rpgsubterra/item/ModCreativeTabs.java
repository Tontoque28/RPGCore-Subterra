package com.tontoque28.rpgsubterra.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ModCreativeTabs {
    public static final ItemGroup SUBTERRA_TAB = new ItemGroup("subterra_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MINER_TIER_3_HELMET.get());
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("creativetab.rpgsubterra.subterra_tab");
        }
    };
}
