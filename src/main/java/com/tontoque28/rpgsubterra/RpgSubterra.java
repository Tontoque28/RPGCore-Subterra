package com.tontoque28.rpgsubterra;

import com.mojang.logging.LogUtils;
import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.item.ModCreativeTabs;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(RpgSubterra.MOD_ID)
public class RpgSubterra {
    public static final String MOD_ID = "rpgsubterra";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RpgSubterra(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        context.registerConfig(ModConfig.Type.COMMON, SubterraCommonConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("RPGCore: Subterra cargado correctamente.");
    }
}