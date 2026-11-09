package com.tontoque28.rpgsubterra.item;

import com.tontoque28.rpgsubterra.RpgSubterra;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;
import java.util.Map;

public enum ModArmorMaterials {
    MINER_TIER_1("miner_tier_1", 1, 13, new int[]{1, 3, 2, 1}, 10,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f, "tier_1_repair",
            0.05f, 8, 2),

    MINER_TIER_2("miner_tier_2", 2, 18, new int[]{2, 4, 3, 1}, 14,
            SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f, "tier_2_repair",
            0.10f, 10, 4),

    MINER_TIER_3("miner_tier_3", 3, 25, new int[]{2, 5, 4, 2}, 15,
            SoundEvents.ARMOR_EQUIP_DIAMOND, 0.5f, 0.0f, "tier_3_repair",
            0.20f, 12, 6),

    MINER_TIER_4("miner_tier_4", 4, 32, new int[]{3, 6, 4, 2}, 18,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 1.5f, 0.05f, "tier_4_repair",
            0.30f, 14, 7),

    MINER_TIER_5("miner_tier_5", 5, 40, new int[]{3, 6, 5, 3}, 22,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 2.5f, 0.10f, "tier_5_repair",
            0.45f, 15, 9);

    private final String name;
    private final int tierLevel;
    private final int durabilityMultiplier;
    private final ArmorMaterial material;
    private final float setMiningSpeedBonus;
    private final int helmetLightLevel;
    private final int lightBeamDistance;

    ModArmorMaterials(String name, int tierLevel, int durabilityMultiplier, int[] protectionAmounts,
                      int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
                      String repairTag, float setMiningSpeedBonus, int helmetLightLevel, int lightBeamDistance) {
        this.name = name;
        this.tierLevel = tierLevel;
        this.durabilityMultiplier = durabilityMultiplier;
        this.setMiningSpeedBonus = setMiningSpeedBonus;
        this.helmetLightLevel = helmetLightLevel;
        this.lightBeamDistance = lightBeamDistance;

        Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);
        defense.put(ArmorType.HELMET, protectionAmounts[0]);
        defense.put(ArmorType.CHESTPLATE, protectionAmounts[1]);
        defense.put(ArmorType.LEGGINGS, protectionAmounts[2]);
        defense.put(ArmorType.BOOTS, protectionAmounts[3]);

        Identifier id = Identifier.fromNamespaceAndPath(RpgSubterra.MOD_ID, name);
        TagKey<Item> tag = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(RpgSubterra.MOD_ID, repairTag));
        ResourceKey<EquipmentAsset> assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, id);
        this.material = new ArmorMaterial(durabilityMultiplier, defense, enchantmentValue, equipSound, toughness,
                knockbackResistance, tag, assetId);
    }

    public ArmorMaterial material() {
        return this.material;
    }

    public int getDurability(ArmorType type) {
        return type.getDurability(this.durabilityMultiplier);
    }

    public String getName() {
        return this.name;
    }

    public int getTierLevel() {
        return this.tierLevel;
    }

    public float getSetMiningSpeedBonus() {
        return this.setMiningSpeedBonus;
    }

    public int getHelmetLightLevel() {
        return this.helmetLightLevel;
    }

    public int getLightBeamDistance() {
        return this.lightBeamDistance;
    }
}
