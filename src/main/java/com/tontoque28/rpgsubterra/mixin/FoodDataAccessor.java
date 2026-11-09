package com.tontoque28.rpgsubterra.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoodData.class)
public interface FoodDataAccessor {

    @Accessor("exhaustionLevel")
    float rpgsubterra$getExhaustion();

    @Accessor("exhaustionLevel")
    void rpgsubterra$setExhaustion(float value);
}
