package com.tontoque28.rpgsubterra.item;

import com.tontoque28.rpgsubterra.RpgSubterra;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    MINER_TIER_1("miner_tier_1", 1, "Fase 1: Cobre y Plata", 13, new int[]{1, 3, 2, 1}, 10,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(createRepairTag("tier_1_repair", Items.COPPER_INGOT)),
            0.05f, 8, 2),

    MINER_TIER_2("miner_tier_2", 2, "Fase 2: Cristales y Oro", 18, new int[]{2, 4, 3, 1}, 14,
            SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f,
            () -> Ingredient.of(createRepairTag("tier_2_repair", Items.GOLD_INGOT)),
            0.10f, 10, 4),

    MINER_TIER_3("miner_tier_3", 3, "Fase 3: Diamante Refinado", 25, new int[]{2, 5, 4, 2}, 15,
            SoundEvents.ARMOR_EQUIP_DIAMOND, 0.5f, 0.0f,
            () -> Ingredient.of(createRepairTag("tier_3_repair", Items.DIAMOND)),
            0.20f, 12, 6),

    MINER_TIER_4("miner_tier_4", 4, "Fase 4: Aleación Abisal", 32, new int[]{3, 6, 4, 2}, 18,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 1.5f, 0.05f,
            () -> Ingredient.of(createRepairTag("tier_4_repair", Items.NETHERITE_INGOT)),
            0.30f, 14, 7),

    MINER_TIER_5("miner_tier_5", 5, "Fase 5: Maestro Dracónico", 40, new int[]{3, 6, 5, 3}, 22,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 2.5f, 0.10f,
            () -> Ingredient.of(createRepairTag("tier_5_repair", Items.ECHO_SHARD)),
            0.45f, 15, 9);

    private static final int[] BASE_DURABILITY = {11, 16, 15, 13};

    private final String name;
    private final int tierLevel;
    private final String phaseName;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;
    private final float setMiningSpeedBonus;
    private final int helmetLightLevel;
    private final int lightBeamDistance;

    ModArmorMaterials(String name, int tierLevel, String phaseName, int durabilityMultiplier, int[] protectionAmounts,
                      int enchantmentValue, SoundEvent equipSound, float toughness, float knockbackResistance,
                      Supplier<Ingredient> repairIngredient, float setMiningSpeedBonus, int helmetLightLevel, int lightBeamDistance) {
        this.name = name;
        this.tierLevel = tierLevel;
        this.phaseName = phaseName;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
        this.setMiningSpeedBonus = setMiningSpeedBonus;
        this.helmetLightLevel = helmetLightLevel;
        this.lightBeamDistance = lightBeamDistance;
    }

    private static TagKey<Item> createRepairTag(String tagName, Item fallbackunused) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(RpgSubterra.MOD_ID, tagName));
    }

    private static int typeIndex(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 0;
            case CHESTPLATE -> 1;
            case LEGGINGS -> 2;
            case BOOTS -> 3;
        };
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[typeIndex(type)] * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionAmounts[typeIndex(type)];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public int getTierLevel() {
        return this.tierLevel;
    }

    public String getPhaseName() {
        return this.phaseName;
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
