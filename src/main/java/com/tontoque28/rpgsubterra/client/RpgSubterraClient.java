package com.tontoque28.rpgsubterra.client;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.ModItems;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderingRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class RpgSubterraClient implements ClientModInitializer {

    @Override
    @SuppressWarnings("unchecked")
    public void onInitializeClient() {
        SpanishLanguageFallback.register();

        List<Item> armors = new ArrayList<>();
        for (Item item : ModItems.all()) {
            if (item instanceof MinerArmorItem) {
                armors.add(item);
            }
        }
        Item[] armorArray = armors.toArray(new Item[0]);

        ArmorRenderingRegistry.registerTexture((entity, stack, slot, secondLayer, suffix, defaultTexture) -> {
            MinerArmorItem armor = (MinerArmorItem) stack.getItem();
            int tier = armor.getMinerMaterial().getTierLevel();
            int layer = slot == EquipmentSlot.LEGS ? 2 : 1;
            return new ResourceLocation(RpgSubterra.MOD_ID, "textures/models/armor/miner_tier_" + tier + "_layer_" + layer + ".png");
        }, armorArray);

        ArmorRenderingRegistry.registerModel((entity, stack, slot, defaultModel) -> {
            MinerArmorItem armor = (MinerArmorItem) stack.getItem();
            return (net.minecraft.client.model.HumanoidModel<net.minecraft.world.entity.LivingEntity>) MinerArmorModel.get(armor.getMinerMaterial().getTierLevel(), slot, defaultModel);
        }, armorArray);
    }
}
