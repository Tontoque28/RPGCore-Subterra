package com.tontoque28.rpgsubterra.event;

import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class SubterraLootEvents {

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> onLootTableLoad(key, tableBuilder));
    }

    private static void onLootTableLoad(ResourceKey<LootTable> name, LootTable.Builder table) {
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

        else if (name.equals(BuiltInLootTables.ANCIENT_CITY)) {
            addItemPool(table, (float) (0.20f * mult),
                    ModItems.RESONANT_CHISEL, ModItems.FORTUNE_GEODE);

            if (SubterraCommonConfig.enableReliquaryCharmUnlock()) {
                addItemPool(table, (float) (0.08f * mult),
                        ModItems.PROSPECTOR_RELIQUARY);
            }
        }
    }

    private static void addItemPool(LootTable.Builder table, float chance, Item... items) {
        LootPool.Builder poolBuilder = LootPool.lootPool()
                .setRolls(ContextIntProviders.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(Math.min(1.0f, chance)));

        for (Item item : items) {
            poolBuilder.add(LootItem.lootTableItem(item).setWeight(1));
        }

        table.pool(poolBuilder.build());
    }
}
