package com.tontoque28.rpgsubterra.item.custom;

import com.tontoque28.rpgsubterra.item.ModArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

public class MinerArmorItem extends Item {
    private static final EquipmentSlot[] ARMOR_SLOTS = {EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD};

    private final ModArmorMaterials minerMaterial;
    private final ArmorType armorType;

    public MinerArmorItem(ModArmorMaterials material, ArmorType type, Properties properties) {
        super(properties.humanoidArmor(material.material(), type));
        this.minerMaterial = material;
        this.armorType = type;
    }

    public ArmorType getArmorType() {
        return this.armorType;
    }

    public ModArmorMaterials getMinerMaterial() {
        return this.minerMaterial;
    }

    public static boolean hasFullMinerSet(Player player, ModArmorMaterials targetMaterial) {
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack armorStack = player.getItemBySlot(slot);
            if (!(armorStack.getItem() instanceof MinerArmorItem minerArmor)) {
                return false;
            }
            if (minerArmor.getMinerMaterial() != targetMaterial) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        int tier = this.minerMaterial.getTierLevel();
        int speedPercent = Math.round(this.minerMaterial.getSetMiningSpeedBonus() * 100);

        tooltip.accept(Component.translatable("tooltip.rpgsubterra.tier_header", toRoman(tier))
                .withStyle(ChatFormatting.GOLD));

        if (this.armorType == ArmorType.HELMET && this.minerMaterial.getHelmetLightLevel() > 0) {
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.helmet_light",
                            this.minerMaterial.getHelmetLightLevel(), this.minerMaterial.getLightBeamDistance())
                    .withStyle(ChatFormatting.YELLOW));
        }

        tooltip.accept(Component.empty());
        tooltip.accept(Component.translatable("tooltip.rpgsubterra.full_set_header").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("tooltip.rpgsubterra.mining_speed", speedPercent).withStyle(ChatFormatting.GREEN));

        if (tier >= 2) {
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.less_fatigue").withStyle(ChatFormatting.AQUA));
        }
        if (tier >= 3) {
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.underwater_fall").withStyle(ChatFormatting.AQUA));
        }
        if (tier >= 4) {
            tooltip.accept(Component.translatable("tooltip.rpgsubterra.lava_protection").withStyle(ChatFormatting.RED));
        }

        super.appendHoverText(stack, context, display, tooltip, flag);
    }

    private static String toRoman(int tier) {
        return switch (tier) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(tier);
        };
    }
}
