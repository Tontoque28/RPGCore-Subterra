package com.tontoque28.rpgsubterra.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.rpgsubterra.curio_header").withStyle(ChatFormatting.DARK_GRAY));

        if (miningSpeedBonus > 0) {
            int pct = Math.round(miningSpeedBonus * 100);
            tooltip.add(Component.translatable("tooltip.rpgsubterra.mining_speed", pct).withStyle(ChatFormatting.GREEN));
        }
        if (extraFortuneChance > 0) {
            int pct = Math.round(extraFortuneChance * 100);
            tooltip.add(Component.translatable("tooltip.rpgsubterra.extra_fortune", pct).withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.translatable("tooltip.rpgsubterra.cave_relic").withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW));
        }
        if (lightLevel > 0) {
            tooltip.add(Component.translatable("tooltip.rpgsubterra.helmet_light", lightLevel, lightDistance)
                    .withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, level, tooltip, flag);
    }
}