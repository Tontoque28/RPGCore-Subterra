package com.tontoque28.rpgsubterra.event;

import com.tontoque28.rpgsubterra.RpgSubterra;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RpgSubterra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SubterraLootEvents {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        double mult = SubterraCommonConfig.CHEST_LOOT_CHANCE_MULTIPLIER.get();
        if (mult <= 0.0) return;

        ResourceLocation name = event.getName();

        if (name.equals(BuiltInLootTables.ABANDONED_MINESHAFT)) {
            addItemPool(event, "subterra_mineshaft_common", (float) (0.25f * mult),
                    ModItems.STONE_CHISEL.get(), ModItems.BELT_CANDLE.get());

            addItemPool(event, "subterra_mineshaft_rare", (float) (0.12f * mult),
                    ModItems.SILVER_CHISEL.get(), ModItems.PYRITE_CLOVER.get(), ModItems.MINER_BELT_LANTERN.get());

            if (SubterraCommonConfig.ENABLE_RELIQUARY_CHARM_UNLOCK.get()) {
                addItemPool(event, "subterra_mineshaft_reliquary", (float) (0.04f * mult),
                        ModItems.PROSPECTOR_RELIQUARY.get());
            }
        }

        else if (name.equals(BuiltInLootTables.SIMPLE_DUNGEON)) {
            addItemPool(event, "subterra_dungeon_loot", (float) (0.18f * mult),
                    ModItems.STONE_CHISEL.get(), ModItems.PYRITE_CLOVER.get(), ModItems.BELT_CANDLE.get());
        }

        else if (name.equals(BuiltInLootTables.ANCIENT_CITY)) {
            addItemPool(event, "subterra_ancient_city_loot", (float) (0.20f * mult),
                    ModItems.RESONANT_CHISEL.get(), ModItems.FORTUNE_GEODE.get());

            if (SubterraCommonConfig.ENABLE_RELIQUARY_CHARM_UNLOCK.get()) {
                addItemPool(event, "subterra_ancient_reliquary", (float) (0.08f * mult),
                        ModItems.PROSPECTOR_RELIQUARY.get());
            }
        }
    }

    private static void addItemPool(LootTableLoadEvent event, String poolName, float chance, Item... items) {
        LootPool.Builder poolBuilder = LootPool.lootPool()
                .name(poolName)
                .setRolls(ConstantValue.exactly(1.0f))
                .when(LootItemRandomChanceCondition.randomChance(Math.min(1.0f, chance)));

        for (Item item : items) {
            poolBuilder.add(LootItem.lootTableItem(item).setWeight(1));
        }

        event.getTable().addPool(poolBuilder.build());
    }
}