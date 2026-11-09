package com.tontoque28.rpgsubterra.item.custom;

import eu.pb4.trinkets.api.component.TrinketDataComponents;
import eu.pb4.trinkets.api.component.TrinketEquippable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class MinerCurioItem extends Item {
    private final float miningSpeedBonus;
    private final float extraFortuneChance;
    private final int lightLevel;
    private final int lightDistance;

    public MinerCurioItem(Properties properties, String slotId, float miningSpeedBonus, float extraFortuneChance, int lightLevel, int lightDistance) {
        super(properties.stacksTo(1).component(TrinketDataComponents.EQUIPMENT, TrinketEquippable.DEFAULT.withSlots(slotId)));
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.rpgsubterra.curio_header").withStyle(ChatFormatting.DARK_GRAY));

        if (miningSpeedBonus > 0) {
            int pct = Math.round(miningSpeedBonus * 100);
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.mining_speed", pct).withStyle(ChatFormatting.GREEN));
        }
        if (extraFortuneChance > 0) {
            int pct = Math.round(extraFortuneChance * 100);
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.extra_fortune", pct).withStyle(ChatFormatting.GOLD));
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.cave_relic").withStyle(ChatFormatting.ITALIC, ChatFormatting.YELLOW));
        }
        if (lightLevel > 0) {
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.helmet_light", lightLevel, lightDistance)
                    .withStyle(ChatFormatting.YELLOW));
        }
        super.appendHoverText(stack, context, display, tooltip, flag);
    }
}
