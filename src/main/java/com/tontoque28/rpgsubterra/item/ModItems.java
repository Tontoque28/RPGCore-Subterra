package com.tontoque28.rpgsubterra.item;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.custom.CharmSlotUnlockerItem;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import com.tontoque28.rpgsubterra.item.custom.MinerCurioItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ModItems {
    private static final List<Item> ALL = new ArrayList<>();

    public static final Item MINER_TIER_1_HELMET = registerArmor("miner_tier_1_helmet",
            ModArmorMaterials.MINER_TIER_1, ArmorType.HELMET, false);
    public static final Item MINER_TIER_1_CHESTPLATE = registerArmor("miner_tier_1_chestplate",
            ModArmorMaterials.MINER_TIER_1, ArmorType.CHESTPLATE, false);
    public static final Item MINER_TIER_1_LEGGINGS = registerArmor("miner_tier_1_leggings",
            ModArmorMaterials.MINER_TIER_1, ArmorType.LEGGINGS, false);
    public static final Item MINER_TIER_1_BOOTS = registerArmor("miner_tier_1_boots",
            ModArmorMaterials.MINER_TIER_1, ArmorType.BOOTS, false);

    public static final Item MINER_TIER_2_HELMET = registerArmor("miner_tier_2_helmet",
            ModArmorMaterials.MINER_TIER_2, ArmorType.HELMET, false);
    public static final Item MINER_TIER_2_CHESTPLATE = registerArmor("miner_tier_2_chestplate",
            ModArmorMaterials.MINER_TIER_2, ArmorType.CHESTPLATE, false);
    public static final Item MINER_TIER_2_LEGGINGS = registerArmor("miner_tier_2_leggings",
            ModArmorMaterials.MINER_TIER_2, ArmorType.LEGGINGS, false);
    public static final Item MINER_TIER_2_BOOTS = registerArmor("miner_tier_2_boots",
            ModArmorMaterials.MINER_TIER_2, ArmorType.BOOTS, false);

    public static final Item MINER_TIER_3_HELMET = registerArmor("miner_tier_3_helmet",
            ModArmorMaterials.MINER_TIER_3, ArmorType.HELMET, false);
    public static final Item MINER_TIER_3_CHESTPLATE = registerArmor("miner_tier_3_chestplate",
            ModArmorMaterials.MINER_TIER_3, ArmorType.CHESTPLATE, false);
    public static final Item MINER_TIER_3_LEGGINGS = registerArmor("miner_tier_3_leggings",
            ModArmorMaterials.MINER_TIER_3, ArmorType.LEGGINGS, false);
    public static final Item MINER_TIER_3_BOOTS = registerArmor("miner_tier_3_boots",
            ModArmorMaterials.MINER_TIER_3, ArmorType.BOOTS, false);

    public static final Item MINER_TIER_4_HELMET = registerArmor("miner_tier_4_helmet",
            ModArmorMaterials.MINER_TIER_4, ArmorType.HELMET, true);
    public static final Item MINER_TIER_4_CHESTPLATE = registerArmor("miner_tier_4_chestplate",
            ModArmorMaterials.MINER_TIER_4, ArmorType.CHESTPLATE, true);
    public static final Item MINER_TIER_4_LEGGINGS = registerArmor("miner_tier_4_leggings",
            ModArmorMaterials.MINER_TIER_4, ArmorType.LEGGINGS, true);
    public static final Item MINER_TIER_4_BOOTS = registerArmor("miner_tier_4_boots",
            ModArmorMaterials.MINER_TIER_4, ArmorType.BOOTS, true);

    public static final Item MINER_TIER_5_HELMET = registerArmor("miner_tier_5_helmet",
            ModArmorMaterials.MINER_TIER_5, ArmorType.HELMET, true);
    public static final Item MINER_TIER_5_CHESTPLATE = registerArmor("miner_tier_5_chestplate",
            ModArmorMaterials.MINER_TIER_5, ArmorType.CHESTPLATE, true);
    public static final Item MINER_TIER_5_LEGGINGS = registerArmor("miner_tier_5_leggings",
            ModArmorMaterials.MINER_TIER_5, ArmorType.LEGGINGS, true);
    public static final Item MINER_TIER_5_BOOTS = registerArmor("miner_tier_5_boots",
            ModArmorMaterials.MINER_TIER_5, ArmorType.BOOTS, true);

    public static final Item STONE_CHISEL = register("stone_chisel", new Item.Properties(),
            props -> new MinerCurioItem(props, 0.04f, 0.0f, 0, 0));

    public static final Item SILVER_CHISEL = register("silver_chisel", new Item.Properties().rarity(Rarity.UNCOMMON),
            props -> new MinerCurioItem(props, 0.08f, 0.0f, 0, 0));

    public static final Item RESONANT_CHISEL = register("resonant_chisel", new Item.Properties().rarity(Rarity.RARE),
            props -> new MinerCurioItem(props, 0.12f, 0.0f, 0, 0));

    public static final Item PYRITE_CLOVER = register("pyrite_clover", new Item.Properties().rarity(Rarity.UNCOMMON),
            props -> new MinerCurioItem(props, 0.0f, 0.10f, 0, 0));

    public static final Item FORTUNE_GEODE = register("fortune_geode", new Item.Properties().rarity(Rarity.RARE),
            props -> new MinerCurioItem(props, 0.0f, 0.20f, 0, 0));

    public static final Item PROSPECTOR_RELIQUARY = register("prospector_reliquary", new Item.Properties().rarity(Rarity.EPIC),
            CharmSlotUnlockerItem::new);

    public static final Item BELT_CANDLE = register("belt_candle", new Item.Properties(),
            props -> new MinerCurioItem(props, 0.0f, 0.0f, 9, 3));

    public static final Item MINER_BELT_LANTERN = register("miner_belt_lantern", new Item.Properties().rarity(Rarity.UNCOMMON),
            props -> new MinerCurioItem(props, 0.0f, 0.0f, 12, 5));

    private static Item register(String name, Item.Properties properties, Function<Item.Properties, Item> factory) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(RpgSubterra.MOD_ID, name);
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        Item registered = Registry.register(BuiltInRegistries.ITEM, id, factory.apply(properties.setId(key)));
        ALL.add(registered);
        return registered;
    }

    private static Item registerArmor(String name, ModArmorMaterials material, ArmorType type, boolean fireResistant) {
        Item.Properties props = new Item.Properties();
        if (fireResistant) {
            props.fireResistant();
        }
        return register(name, props, p -> new MinerArmorItem(material, type, p));
    }

    public static List<Item> all() {
        return Collections.unmodifiableList(ALL);
    }

    public static void register() {
    }
}
