package com.tontoque28.rpgsubterra;

import com.tontoque28.rpgsubterra.block.ModBlocks;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.data.CharmUnlocks;
import com.tontoque28.rpgsubterra.event.MiningBonusEvents;
import com.tontoque28.rpgsubterra.event.SubterraLootEvents;
import com.tontoque28.rpgsubterra.item.ModItems;
import dev.emi.trinkets.api.TrinketSlots;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RpgSubterra implements ModInitializer {
    public static final String MOD_ID = "rpgsubterra";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        SubterraCommonConfig.load();

        registerSlots();

        ModBlocks.register();
        ModItems.register();

        MiningBonusEvents.register();
        SubterraLootEvents.register();
        CharmUnlocks.register();

        LOGGER.info("RPGCore: Subterra cargado correctamente.");
    }

    private static void registerSlots() {
        TrinketSlots.addSlot("head", "hat", new ResourceLocation("trinkets", "textures/item/empty_trinket_slot_mask.png"));
        TrinketSlots.addSlot("legs", "belt", new ResourceLocation("trinkets", "textures/item/empty_trinket_slot_belt.png"));
        TrinketSlots.addSlot("chest", "charm", new ResourceLocation("trinkets", "textures/item/empty_trinket_slot_charm.png"));
        TrinketSlots.addSlot("chest", "extracharm", new ResourceLocation("trinkets", "textures/item/empty_trinket_slot_charm.png"));
    }
}
