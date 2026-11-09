package com.tontoque28.rpgsubterra.block;

import com.tontoque28.rpgsubterra.RpgSubterra;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class ModBlocks {
    public static final Block LIGHT = Registry.register(Registry.BLOCK,
            new ResourceLocation(RpgSubterra.MOD_ID, "light"), new SubterraLightBlock());

    public static void register() {
    }
}
