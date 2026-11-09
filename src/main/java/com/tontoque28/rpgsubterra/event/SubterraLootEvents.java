package com.tontoque28.rpgsubterra.event;

import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.ConstantIntValue;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class SubterraLootEvents {

    private static final ResourceLocation ANCIENT_CITY = new ResourceLocation("minecraft", "chests/ancient_city");

    public static void register() {
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> onLootTableLoad(id, supplier));
    }

    private static void onLootTableLoad(ResourceLocation name, FabricLootSupplierBuilder table) {
        SubterraCommonConfig.forceRefresh();
        double mult = SubterraCommonConfig.chestLootChanceMultiplier();
        if (mult <= 0.0) return;

        if (name.equals(BuiltInLootTables.ABANDONED_MINESHAFT)) {
            addItemPool(table, (float) (0.25f * mult),
                    ModItems.STONE_CHISEL, ModItems.BELT_CANDLE);

            addItemPool(table, (float) (0.12f * mult),
                    ModItems.SILVER_CHISEL, ModItems.PYRITE_CLOVER, ModItems.MINER_BELT_LANTERN);

            if (SubterraCommonConfig.enableReliquaryCharmUnlock()) {
                addItemPool(table, (float) (0.04f * mult),
                        ModItems.PROSPECTOR_RELIQUARY);
            }
        }

        else if (name.equals(BuiltInLootTables.SIMPLE_DUNGEON)) {
            addItemPool(table, (float) (0.18f * mult),
                    ModItems.STONE_CHISEL, ModItems.PYRITE_CLOVER, ModItems.BELT_CANDLE);
        }

        else if (name.equals(ANCIENT_CITY) || name.equals(BuiltInLootTables.STRONGHOLD_LIBRARY)) {
            addItemPool(table, (float) (0.20f * mult),
                    ModItems.RESONANT_CHISEL, ModItems.FORTUNE_GEODE);

            if (SubterraCommonConfig.enableReliquaryCharmUnlock()) {
                addItemPool(table, (float) (0.08f * mult),
                        ModItems.PROSPECTOR_RELIQUARY);
            }
        }
    }

    private static void addItemPool(FabricLootSupplierBuilder table, float chance, Item... items) {
        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
                .rolls(ConstantIntValue.exactly(1))
                .withCondition(LootItemRandomChanceCondition.randomChance(Math.min(1.0f, chance)).build());

        for (Item item : items) {
            poolBuilder.withEntry(LootItem.lootTableItem(item).setWeight(1).build());
        }

        LootPool pool = poolBuilder.build();
        table.withPool(pool);
    }
}
