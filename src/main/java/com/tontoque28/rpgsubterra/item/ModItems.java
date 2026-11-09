package com.tontoque28.rpgsubterra.item;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.custom.CharmSlotUnlockerItem;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import com.tontoque28.rpgsubterra.item.custom.MinerCurioItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModItems {
    private static final List<Item> ALL = new ArrayList<>();

    public static final Item MINER_TIER_1_HELMET = registerArmor("miner_tier_1_helmet",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlot.HEAD, false);
    public static final Item MINER_TIER_1_CHESTPLATE = registerArmor("miner_tier_1_chestplate",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlot.CHEST, false);
    public static final Item MINER_TIER_1_LEGGINGS = registerArmor("miner_tier_1_leggings",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlot.LEGS, false);
    public static final Item MINER_TIER_1_BOOTS = registerArmor("miner_tier_1_boots",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlot.FEET, false);

    public static final Item MINER_TIER_2_HELMET = registerArmor("miner_tier_2_helmet",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlot.HEAD, false);
    public static final Item MINER_TIER_2_CHESTPLATE = registerArmor("miner_tier_2_chestplate",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlot.CHEST, false);
    public static final Item MINER_TIER_2_LEGGINGS = registerArmor("miner_tier_2_leggings",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlot.LEGS, false);
    public static final Item MINER_TIER_2_BOOTS = registerArmor("miner_tier_2_boots",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlot.FEET, false);

    public static final Item MINER_TIER_3_HELMET = registerArmor("miner_tier_3_helmet",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlot.HEAD, false);
    public static final Item MINER_TIER_3_CHESTPLATE = registerArmor("miner_tier_3_chestplate",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlot.CHEST, false);
    public static final Item MINER_TIER_3_LEGGINGS = registerArmor("miner_tier_3_leggings",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlot.LEGS, false);
    public static final Item MINER_TIER_3_BOOTS = registerArmor("miner_tier_3_boots",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlot.FEET, false);

    public static final Item MINER_TIER_4_HELMET = registerArmor("miner_tier_4_helmet",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlot.HEAD, true);
    public static final Item MINER_TIER_4_CHESTPLATE = registerArmor("miner_tier_4_chestplate",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlot.CHEST, true);
    public static final Item MINER_TIER_4_LEGGINGS = registerArmor("miner_tier_4_leggings",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlot.LEGS, true);
    public static final Item MINER_TIER_4_BOOTS = registerArmor("miner_tier_4_boots",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlot.FEET, true);

    public static final Item MINER_TIER_5_HELMET = registerArmor("miner_tier_5_helmet",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlot.HEAD, true);
    public static final Item MINER_TIER_5_CHESTPLATE = registerArmor("miner_tier_5_chestplate",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlot.CHEST, true);
    public static final Item MINER_TIER_5_LEGGINGS = registerArmor("miner_tier_5_leggings",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlot.LEGS, true);
    public static final Item MINER_TIER_5_BOOTS = registerArmor("miner_tier_5_boots",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlot.FEET, true);

    public static final Item STONE_CHISEL = register("stone_chisel",
            new MinerCurioItem(props(), 0.04f, 0.0f, 0, 0, "legs", "belt"));

    public static final Item SILVER_CHISEL = register("silver_chisel",
            new MinerCurioItem(props().rarity(Rarity.UNCOMMON), 0.08f, 0.0f, 0, 0, "legs", "belt"));

    public static final Item RESONANT_CHISEL = register("resonant_chisel",
            new MinerCurioItem(props().rarity(Rarity.RARE), 0.12f, 0.0f, 0, 0, "legs", "belt"));

    public static final Item PYRITE_CLOVER = register("pyrite_clover",
            new MinerCurioItem(props().rarity(Rarity.UNCOMMON), 0.0f, 0.10f, 0, 0, "chest", "charm"));

    public static final Item FORTUNE_GEODE = register("fortune_geode",
            new MinerCurioItem(props().rarity(Rarity.RARE), 0.0f, 0.20f, 0, 0, "chest", "charm"));

    public static final Item PROSPECTOR_RELIQUARY = register("prospector_reliquary",
            new CharmSlotUnlockerItem(props().rarity(Rarity.EPIC)));

    public static final Item BELT_CANDLE = register("belt_candle",
            new MinerCurioItem(props(), 0.0f, 0.0f, 9, 3, "head", "hat"));

    public static final Item MINER_BELT_LANTERN = register("miner_belt_lantern",
            new MinerCurioItem(props().rarity(Rarity.UNCOMMON), 0.0f, 0.0f, 12, 5, "head", "hat"));

    private static Item.Properties props() {
        return new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB);
    }

    private static Item register(String name, Item item) {
        Item registered = Registry.register(Registry.ITEM, new ResourceLocation(RpgSubterra.MOD_ID, name), item);
        ALL.add(registered);
        return registered;
    }

    private static Item registerArmor(String name, ModArmorMaterials material, EquipmentSlot type, boolean fireResistant) {
        Item.Properties props = props();
        if (fireResistant) {
            props.fireResistant();
        }
        return register(name, new MinerArmorItem(material, type, props));
    }

    public static List<Item> all() {
        return Collections.unmodifiableList(ALL);
    }

    public static void register() {
    }
}
