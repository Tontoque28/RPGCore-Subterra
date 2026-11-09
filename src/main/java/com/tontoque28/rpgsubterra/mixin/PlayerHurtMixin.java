package com.tontoque28.rpgsubterra.mixin;

import com.tontoque28.rpgsubterra.event.MiningBonusEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerHurtMixin extends LivingEntity {

    protected PlayerHurtMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"))
    private float rpgsubterra$modifyHurtAmount(Player instance, DamageSource source, float amount) {
        float modified = MiningBonusEvents.modifyHurtAmount((Player) (Object) this, source, amount);
        return this.getDamageAfterArmorAbsorb(source, modified);
    }
}
