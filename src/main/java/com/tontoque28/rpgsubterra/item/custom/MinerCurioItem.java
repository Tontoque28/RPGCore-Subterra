package com.tontoque28.rpgsubterra.item.custom;

import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class MinerCurioItem extends TrinketItem {
    private final float miningSpeedBonus;
    private final float extraFortuneChance;
    private final int lightLevel;
    private final int lightDistance;
    private final String group;
    private final String slot;

    public MinerCurioItem(Properties properties, float miningSpeedBonus, float extraFortuneChance, int lightLevel, int lightDistance, String group, String slot) {
        super(properties.stacksTo(1));
        this.miningSpeedBonus = miningSpeedBonus;
        this.extraFortuneChance = extraFortuneChance;
        this.lightLevel = lightLevel;
        this.lightDistance = lightDistance;
        this.group = group;
        this.slot = slot;
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
    public boolean canWearInSlot(String group, String slot) {
        if (!this.group.equals(group)) {
            return false;
        }
        if (this.slot.equals(slot)) {
            return true;
        }
        return "charm".equals(this.slot) && "extracharm".equals(slot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.curio_header").withStyle(ChatFormatting.DARK_GRAY));

        if (miningSpeedBonus > 0) {
            int pct = Math.round(miningSpeedBonus * 100);
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.mining_speed", pct).withStyle(ChatFormatting.GREEN));
        }
        if (extraFortuneChance > 0) {
            int pct = Math.round(extraFortuneChance * 100);
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.extra_fortune", pct).withStyle(ChatFormatting.GOLD));
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.cave_relic").withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW));
        }
        if (lightLevel > 0) {
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.helmet_light", lightLevel, lightDistance)
                    .withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
