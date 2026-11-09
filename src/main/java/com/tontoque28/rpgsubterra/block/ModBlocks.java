package com.tontoque28.rpgsubterra.block;

import com.tontoque28.rpgsubterra.RpgSubterra;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RpgSubterra.MOD_ID);

    public static final RegistryObject<Block> LIGHT = BLOCKS.register("light", SubterraLightBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
