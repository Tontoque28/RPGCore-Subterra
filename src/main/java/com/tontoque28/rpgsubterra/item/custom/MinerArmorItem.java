package com.tontoque28.rpgsubterra.item.custom;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.client.MinerArmorModel;
import com.tontoque28.rpgsubterra.item.ModArmorMaterials;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MinerArmorItem extends ArmorItem {
    private final ModArmorMaterials minerMaterial;

    public MinerArmorItem(ModArmorMaterials material, EquipmentSlotType type, Properties properties) {
        super(material, type, properties);
        this.minerMaterial = material;
    }

    public ModArmorMaterials getMinerMaterial() {
        return this.minerMaterial;
    }

    public static boolean hasFullMinerSet(PlayerEntity player, ModArmorMaterials targetMaterial) {
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
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltip, ITooltipFlag flag) {
        int tier = this.minerMaterial.getTierLevel();
        int speedPercent = Math.round(this.minerMaterial.getSetMiningSpeedBonus() * 100);

        tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.tier_header", toRoman(tier))
                .withStyle(TextFormatting.GOLD));

        if (this.slot == EquipmentSlotType.HEAD && this.minerMaterial.getHelmetLightLevel() > 0) {
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.helmet_light",
                            this.minerMaterial.getHelmetLightLevel(), this.minerMaterial.getLightBeamDistance())
                    .withStyle(TextFormatting.YELLOW));
        }

        tooltip.add(StringTextComponent.EMPTY);
        tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.full_set_header").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.mining_speed", speedPercent).withStyle(TextFormatting.GREEN));

        if (tier >= 2) {
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.less_fatigue").withStyle(TextFormatting.AQUA));
        }
        if (tier >= 3) {
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.underwater_fall").withStyle(TextFormatting.AQUA));
        }
        if (tier >= 4) {
            tooltip.add(new TranslationTextComponent("tooltip.rpgsubterra.lava_protection").withStyle(TextFormatting.RED));
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

    @Override
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        int tier = this.minerMaterial.getTierLevel();
        int layer = (slot == EquipmentSlotType.LEGS) ? 2 : 1;
        return RpgSubterra.MOD_ID + ":textures/models/armor/miner_tier_" + tier + "_layer_" + layer + ".png";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlotType armorSlot, A original) {
        return (A) MinerArmorModel.get(minerMaterial.getTierLevel(), armorSlot, original);
    }
}
