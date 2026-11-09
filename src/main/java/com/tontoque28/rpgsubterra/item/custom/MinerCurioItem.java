package com.tontoque28.rpgsubterra.item.custom;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public class MinerCurioItem extends Item implements ICurioItem {
    private final float miningSpeedBonus;
    private final float extraFortuneChance;
    private final int lightLevel;
    private final int lightDistance;

    public MinerCurioItem(Properties properties, float miningSpeedBonus, float extraFortuneChance, int lightLevel, int lightDistance) {
        super(properties.stacksTo(1));
        this.miningSpeedBonus = miningSpeedBonus;
        this.extraFortuneChance = extraFortuneChance;
        this.lightLevel = lightLevel;
        this.lightDistance = lightDistance;
    }

    public float getMiningSpeedBonus() {
        return miningSpeedBonus;
    }

    public float getExtraFortuneChance() {
        return extraFortuneChance;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public int getLightDistance() {
        return lightDistance;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.curio_header").withStyle(TextFormatting.DARK_GRAY));

        if (miningSpeedBonus > 0) {
            int pct = Math.round(miningSpeedBonus * 100);
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.mining_speed", pct).withStyle(TextFormatting.GREEN));
        }
        if (extraFortuneChance > 0) {
            int pct = Math.round(extraFortuneChance * 100);
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.extra_fortune", pct).withStyle(TextFormatting.GOLD));
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.cave_relic").withStyle(TextFormatting.ITALIC, TextFormatting.YELLOW));
        }
        if (lightLevel > 0) {
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.helmet_light", lightLevel, lightDistance)
                    .withStyle(TextFormatting.YELLOW));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
