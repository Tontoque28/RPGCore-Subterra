package com.tontoque28.rpgsubterra.mixin;

import com.tontoque28.rpgsubterra.event.MiningBonusEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMiningSpeedMixin {

    @Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
    private void rpgsubterra$modifyDestroySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(MiningBonusEvents.modifyBreakSpeed((Player) (Object) this, cir.getReturnValueF()));
    }
}
