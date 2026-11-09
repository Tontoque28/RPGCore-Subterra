package com.tontoque28.rpgsubterra.item;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.item.custom.CharmSlotUnlockerItem;
import com.tontoque28.rpgsubterra.item.custom.MinerArmorItem;
import com.tontoque28.rpgsubterra.item.custom.MinerCurioItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RpgSubterra.MOD_ID);

    public static final RegistryObject<Item> MINER_TIER_1_HELMET = registerArmor("miner_tier_1_helmet",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlotType.HEAD, false);
    public static final RegistryObject<Item> MINER_TIER_1_CHESTPLATE = registerArmor("miner_tier_1_chestplate",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlotType.CHEST, false);
    public static final RegistryObject<Item> MINER_TIER_1_LEGGINGS = registerArmor("miner_tier_1_leggings",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlotType.LEGS, false);
    public static final RegistryObject<Item> MINER_TIER_1_BOOTS = registerArmor("miner_tier_1_boots",
            ModArmorMaterials.MINER_TIER_1, EquipmentSlotType.FEET, false);

    public static final RegistryObject<Item> MINER_TIER_2_HELMET = registerArmor("miner_tier_2_helmet",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlotType.HEAD, false);
    public static final RegistryObject<Item> MINER_TIER_2_CHESTPLATE = registerArmor("miner_tier_2_chestplate",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlotType.CHEST, false);
    public static final RegistryObject<Item> MINER_TIER_2_LEGGINGS = registerArmor("miner_tier_2_leggings",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlotType.LEGS, false);
    public static final RegistryObject<Item> MINER_TIER_2_BOOTS = registerArmor("miner_tier_2_boots",
            ModArmorMaterials.MINER_TIER_2, EquipmentSlotType.FEET, false);

    public static final RegistryObject<Item> MINER_TIER_3_HELMET = registerArmor("miner_tier_3_helmet",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlotType.HEAD, false);
    public static final RegistryObject<Item> MINER_TIER_3_CHESTPLATE = registerArmor("miner_tier_3_chestplate",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlotType.CHEST, false);
    public static final RegistryObject<Item> MINER_TIER_3_LEGGINGS = registerArmor("miner_tier_3_leggings",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlotType.LEGS, false);
    public static final RegistryObject<Item> MINER_TIER_3_BOOTS = registerArmor("miner_tier_3_boots",
            ModArmorMaterials.MINER_TIER_3, EquipmentSlotType.FEET, false);

    public static final RegistryObject<Item> MINER_TIER_4_HELMET = registerArmor("miner_tier_4_helmet",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlotType.HEAD, true);
    public static final RegistryObject<Item> MINER_TIER_4_CHESTPLATE = registerArmor("miner_tier_4_chestplate",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlotType.CHEST, true);
    public static final RegistryObject<Item> MINER_TIER_4_LEGGINGS = registerArmor("miner_tier_4_leggings",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlotType.LEGS, true);
    public static final RegistryObject<Item> MINER_TIER_4_BOOTS = registerArmor("miner_tier_4_boots",
            ModArmorMaterials.MINER_TIER_4, EquipmentSlotType.FEET, true);

    public static final RegistryObject<Item> MINER_TIER_5_HELMET = registerArmor("miner_tier_5_helmet",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlotType.HEAD, true);
    public static final RegistryObject<Item> MINER_TIER_5_CHESTPLATE = registerArmor("miner_tier_5_chestplate",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlotType.CHEST, true);
    public static final RegistryObject<Item> MINER_TIER_5_LEGGINGS = registerArmor("miner_tier_5_leggings",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlotType.LEGS, true);
    public static final RegistryObject<Item> MINER_TIER_5_BOOTS = registerArmor("miner_tier_5_boots",
            ModArmorMaterials.MINER_TIER_5, EquipmentSlotType.FEET, true);

    public static final RegistryObject<Item> STONE_CHISEL = ITEMS.register("stone_chisel",
            () -> new MinerCurioItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB), 0.04f, 0.0f, 0, 0));

    public static final RegistryObject<Item> SILVER_CHISEL = ITEMS.register("silver_chisel",
            () -> new MinerCurioItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB).rarity(Rarity.UNCOMMON), 0.08f, 0.0f, 0, 0));

    public static final RegistryObject<Item> RESONANT_CHISEL = ITEMS.register("resonant_chisel",
            () -> new MinerCurioItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB).rarity(Rarity.RARE), 0.12f, 0.0f, 0, 0));

    public static final RegistryObject<Item> PYRITE_CLOVER = ITEMS.register("pyrite_clover",
            () -> new MinerCurioItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB).rarity(Rarity.UNCOMMON), 0.0f, 0.10f, 0, 0));

    public static final RegistryObject<Item> FORTUNE_GEODE = ITEMS.register("fortune_geode",
            () -> new MinerCurioItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB).rarity(Rarity.RARE), 0.0f, 0.20f, 0, 0));

    public static final RegistryObject<Item> PROSPECTOR_RELIQUARY = ITEMS.register("prospector_reliquary",
            () -> new CharmSlotUnlockerItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> BELT_CANDLE = ITEMS.register("belt_candle",
            () -> new MinerCurioItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB), 0.0f, 0.0f, 9, 3));

    public static final RegistryObject<Item> MINER_BELT_LANTERN = ITEMS.register("miner_belt_lantern",
            () -> new MinerCurioItem(new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB).rarity(Rarity.UNCOMMON), 0.0f, 0.0f, 12, 5));

    private static RegistryObject<Item> registerArmor(String name, ModArmorMaterials material, EquipmentSlotType type, boolean fireResistant) {
        return ITEMS.register(name, () -> {
            Item.Properties props = new Item.Properties().tab(ModCreativeTabs.SUBTERRA_TAB);
            if (fireResistant) {
                props.fireResistant();
            }
            return new MinerArmorItem(material, type, props);
        });
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}