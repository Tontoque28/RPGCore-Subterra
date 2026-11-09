package com.tontoque28.rpgsubterra;

import com.tontoque28.rpgsubterra.config.SubterraCommonConfig;
import com.tontoque28.rpgsubterra.block.ModBlocks;
import com.tontoque28.rpgsubterra.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod(RpgSubterra.MOD_ID)
public class RpgSubterra {
    public static final String MOD_ID = "rpgsubterra";
    public static final Logger LOGGER = LogManager.getLogger();

    public RpgSubterra() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        modEventBus.addListener(this::enqueueSlots);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SubterraCommonConfig.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("RPGCore: Subterra cargado correctamente.");
    }

    private void enqueueSlots(InterModEnqueueEvent event) {
        for (SlotTypePreset preset : new SlotTypePreset[]{SlotTypePreset.HEAD, SlotTypePreset.CHARM, SlotTypePreset.BELT, SlotTypePreset.HANDS}) {
            InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> preset.getMessageBuilder().build());
        }
    }
}
