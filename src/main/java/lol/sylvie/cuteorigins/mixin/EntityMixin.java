package lol.sylvie.cuteorigins.mixin;

import lol.sylvie.cuteorigins.origin.Origin;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.power.effect.impl.CannotSeeEffect;
import lol.sylvie.cuteorigins.power.effect.impl.InvisibleEffect;
import lol.sylvie.cuteorigins.state.StateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Unique
    private boolean isInvisibleTo(ServerPlayerEntity observer) {
        Origin origin = StateManager.getPlayerState(observer).getOrigin();
        if (origin == null) return false;
        CannotSeeEffect effect = (CannotSeeEffect) origin.getFirstEffect(CannotSeeEffect.class);

        if (effect == null) return false;
        return effect.getCondition().test((Entity) (Object) this);
    }

    @Inject(method = "canBeSpectated", at = @At("HEAD"), cancellable = true)
    public void cuteorigins_broadcast(ServerPlayerEntity spectator, CallbackInfoReturnable<Boolean> cir) {
        if (isInvisibleTo(spectator)) cir.setReturnValue(false);
    }

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    public void cuteorigins_invisibleTo(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if ((player instanceof ServerPlayerEntity serverPlayer) && isInvisibleTo(serverPlayer)) cir.setReturnValue(false);
    }

    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    public void cuteorigins_invisible(CallbackInfoReturnable<Boolean> cir) {
        Entity thisEntity = (Entity) (Object) this;
        if (thisEntity instanceof PlayerEntity player) {
            Origin origin = StateManager.getPlayerState(player).getOrigin();
            if (origin == null) return;
            for (Effect effect : origin.getEffectsOfType(InvisibleEffect.class)) {
                InvisibleEffect invisibleEffect = (InvisibleEffect) effect;
                if (invisibleEffect.getCondition().test(player)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}
