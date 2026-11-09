package com.tontoque28.rpgsubterra.item.custom;

import com.tontoque28.rpgsubterra.item.ModArmorMaterials;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class MinerArmorItem extends ArmorItem {
    private final ModArmorMaterials minerMaterial;

    public MinerArmorItem(ModArmorMaterials material, EquipmentSlot type, Properties properties) {
        super(material, type, properties);
        this.minerMaterial = material;
    }

    public ModArmorMaterials getMinerMaterial() {
        return this.minerMaterial;
    }

    public static boolean hasFullMinerSet(Player player, ModArmorMaterials targetMaterial) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (!(armorStack.getItem() instanceof MinerArmorItem)) {
                return false;
            }
            MinerArmorItem minerArmor = (MinerArmorItem) armorStack.getItem();
            if (minerArmor.getMinerMaterial() != targetMaterial) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        int tier = this.minerMaterial.getTierLevel();
        int speedPercent = Math.round(this.minerMaterial.getSetMiningSpeedBonus() * 100);

        tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.tier_header", toRoman(tier))
                .withStyle(ChatFormatting.GOLD));

        if (this.slot == EquipmentSlot.HEAD && this.minerMaterial.getHelmetLightLevel() > 0) {
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.helmet_light",
                            this.minerMaterial.getHelmetLightLevel(), this.minerMaterial.getLightBeamDistance())
                    .withStyle(ChatFormatting.YELLOW));
        }

        tooltip.add(TextComponent.EMPTY);
        tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.full_set_header").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.mining_speed", speedPercent).withStyle(ChatFormatting.GREEN));

        if (tier >= 2) {
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.less_fatigue").withStyle(ChatFormatting.AQUA));
        }
        if (tier >= 3) {
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.underwater_fall").withStyle(ChatFormatting.AQUA));
        }
        if (tier >= 4) {
            tooltip.add(new TranslatableComponent("tooltip.rpgsubterra.lava_protection").withStyle(ChatFormatting.RED));
        }

        super.appendHoverText(stack, level, tooltip, flag);
    }

    private static String toRoman(int tier) {
        switch (tier) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            default:
                return String.valueOf(tier);
        }
    }
}
