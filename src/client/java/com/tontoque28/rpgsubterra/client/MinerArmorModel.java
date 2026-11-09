package com.tontoque28.rpgsubterra.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MinerArmorModel {
    private static final Map<Integer, Map<EquipmentSlot, HumanoidModel<HumanoidRenderState>>> CACHE = new HashMap<>();

    public static HumanoidModel<HumanoidRenderState> get(int tier, EquipmentSlot slot, HumanoidModel<HumanoidRenderState> original) {
        HumanoidModel<HumanoidRenderState> model = CACHE
                .computeIfAbsent(tier, t -> new EnumMap<>(EquipmentSlot.class))
                .computeIfAbsent(slot, s -> buildModel(tier, s));

        original.copyPropertiesTo(model);
        model.head.copyFrom(original.head);
        model.body.copyFrom(original.body);
        model.rightArm.copyFrom(original.rightArm);
        model.leftArm.copyFrom(original.leftArm);
        model.rightLeg.copyFrom(original.rightLeg);
        model.leftLeg.copyFrom(original.leftLeg);

        model.setAllVisible(false);
        switch (slot) {
            case HEAD -> model.head.visible = true;
            case CHEST -> {
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
            }
            case LEGS -> {
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            default -> {}
        }
        return model;
    }

    private static HumanoidModel<HumanoidRenderState> buildModel(int tier, EquipmentSlot slot) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeDeformation def = new CubeDeformation(slot == EquipmentSlot.LEGS ? 0.45F : 0.85F);

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, def), PartPose.ZERO);
        root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

        root.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, def), PartPose.ZERO);

        root.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, def), PartPose.offset(-5.0F, 2.0F, 0.0F));

        root.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, def), PartPose.offset(5.0F, 2.0F, 0.0F));

        root.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, def), PartPose.offset(-1.9F, 12.0F, 0.0F));

        root.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, def), PartPose.offset(1.9F, 12.0F, 0.0F));

        if (slot == EquipmentSlot.HEAD) {
            if (tier == 1) {
                head.addOrReplaceChild("candle_wax", CubeListBuilder.create()
                        .texOffs(56, 0).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.ZERO);
                head.addOrReplaceChild("candle_flame", CubeListBuilder.create()
                        .texOffs(56, 6).addBox(-0.5F, -13.6F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
            } else if (tier == 5) {

                head.addOrReplaceChild("horn_r", CubeListBuilder.create()
                                .texOffs(44, 0).addBox(-1.5F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F)
                                .texOffs(44, 6).addBox(-2.8F, -4.5F, -1.0F, 2.0F, 4.0F, 2.0F)
                                .texOffs(52, 6).addBox(-3.8F, -7.5F, -0.5F, 2.0F, 4.0F, 2.0F),
                        PartPose.offsetAndRotation(-4.2F, -6.5F, -0.5F, 0.22F, 0.0F, -0.28F));

                head.addOrReplaceChild("horn_l", CubeListBuilder.create()
                                .texOffs(44, 0).mirror().addBox(-0.5F, -1.5F, -1.5F, 2.0F, 3.0F, 3.0F)
                                .texOffs(44, 6).mirror().addBox(0.8F, -4.5F, -1.0F, 2.0F, 4.0F, 2.0F)
                                .texOffs(52, 6).mirror().addBox(1.8F, -7.5F, -0.5F, 2.0F, 4.0F, 2.0F),
                        PartPose.offsetAndRotation(4.2F, -6.5F, -0.5F, 0.22F, 0.0F, 0.28F));
            }
        }

        ModelPart bakedRoot = LayerDefinition.create(mesh, 64, 32).bakeRoot();
        return new HumanoidModel<>(bakedRoot);
    }

}