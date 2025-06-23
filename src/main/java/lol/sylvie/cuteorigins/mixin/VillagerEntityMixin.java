package lol.sylvie.cuteorigins.mixin;

import lol.sylvie.cuteorigins.origin.Origin;
import lol.sylvie.cuteorigins.power.effect.impl.VillagerGossipEffect;
import lol.sylvie.cuteorigins.state.StateManager;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerEntity.class)
public class VillagerEntityMixin {
    @Inject(method = "getReputation", at = @At("RETURN"), cancellable = true)
    public void origins$modifyReputation(PlayerEntity player, CallbackInfoReturnable<Integer> cir) {
        if (!(player instanceof ServerPlayerEntity)) return;
        Origin origin = StateManager.getPlayerState(player).getOrigin();
        if (origin == null) return;

        int original = cir.getReturnValue();
        int sumOffset = origin.getEffectsOfType(VillagerGossipEffect.class).stream().mapToInt(m -> ((VillagerGossipEffect) m).getReputation()).sum();
        cir.setReturnValue(original + sumOffset);
    }
}
