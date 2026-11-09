package com.tontoque28.rpgsubterra;

import com.mojang.logging.LogUtils;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.event.MiningBonusEvents;
import com.tontoque28.rpgsubterra.event.SubterraLootEvents;
import com.tontoque28.rpgsubterra.item.ModCreativeTabs;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class RpgSubterra implements ModInitializer {
    public static final String MOD_ID = "rpgsubterra";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        SubterraCommonConfig.load();

        ModItems.register();
        ModCreativeTabs.register();

        MiningBonusEvents.register();
        SubterraLootEvents.register();

        LOGGER.info("RPGCore: Subterra cargado correctamente.");
    }
}
