package com.tontoque28.rpgsubterra.client;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MinerArmorModel {
    private static final Map<Integer, Map<EquipmentSlotType, BipedModel<LivingEntity>>> CACHE = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static BipedModel<?> get(int tier, EquipmentSlotType slot, BipedModel<?> original) {
        BipedModel<LivingEntity> model = CACHE
                .computeIfAbsent(tier, t -> new EnumMap<>(EquipmentSlotType.class))
                .computeIfAbsent(slot, s -> buildModel(tier, s));

        ((BipedModel<LivingEntity>) original).copyPropertiesTo(model);
        model.head.copyFrom(original.head);
        model.body.copyFrom(original.body);
        model.rightArm.copyFrom(original.rightArm);
        model.leftArm.copyFrom(original.leftArm);
        model.rightLeg.copyFrom(original.rightLeg);
        model.leftLeg.copyFrom(original.leftLeg);

        model.setAllVisible(false);
        switch (slot) {
            case HEAD:
                model.head.visible = true;
                break;
            case CHEST:
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
                break;
            case LEGS:
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                break;
            case FEET:
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
                break;
            default:
                break;
        }
        return model;
    }

    private static BipedModel<LivingEntity> buildModel(int tier, EquipmentSlotType slot) {
        BipedModel<LivingEntity> model = new BipedModel<>(slot == EquipmentSlotType.LEGS ? 0.45F : 0.85F);

        if (slot == EquipmentSlotType.HEAD) {
            if (tier == 1) {
                ModelRenderer wax = new ModelRenderer(model, 56, 0);
                wax.addBox(-1.0F, -12.0F, -1.0F, 2.0F, 3.0F, 2.0F);
                model.head.addChild(wax);
                ModelRenderer flame = new ModelRenderer(model, 56, 6);
                flame.addBox(-0.5F, -13.6F, -0.5F, 1.0F, 2.0F, 1.0F);
                model.head.addChild(flame);
            } else if (tier == 5) {
                ModelRenderer hornRight = new ModelRenderer(model);
                hornRight.texOffs(44, 0).addBox(-1.5F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F);
                hornRight.texOffs(44, 6).addBox(-2.8F, -4.5F, -1.0F, 2.0F, 4.0F, 2.0F);
                hornRight.texOffs(52, 6).addBox(-3.8F, -7.5F, -0.5F, 2.0F, 4.0F, 2.0F);
                hornRight.setPos(-4.2F, -6.5F, -0.5F);
                hornRight.xRot = 0.22F;
                hornRight.yRot = 0.0F;
                hornRight.zRot = -0.28F;
                model.head.addChild(hornRight);

                ModelRenderer hornLeft = new ModelRenderer(model);
                hornLeft.mirror = true;
                hornLeft.texOffs(44, 0).addBox(-0.5F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F);
                hornLeft.texOffs(44, 6).addBox(0.8F, -4.5F, -1.0F, 2.0F, 4.0F, 2.0F);
                hornLeft.texOffs(52, 6).addBox(1.8F, -7.5F, -0.5F, 2.0F, 4.0F, 2.0F);
                hornLeft.setPos(4.2F, -6.5F, -0.5F);
                hornLeft.xRot = 0.22F;
                hornLeft.yRot = 0.0F;
                hornLeft.zRot = 0.28F;
                model.head.addChild(hornLeft);
            }
        }

        return model;
    }
}
