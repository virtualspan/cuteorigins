package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.mixininterfaces.Phasable;
import lol.sylvie.cuteorigins.power.effect.Effect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class TogglePhasingEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("toggle_phasing");

    protected TogglePhasingEffect() {
        super(IDENTIFIER, true);
    }

    @Override
    public void onAction(ServerPlayerEntity player) {
        Phasable phasable = player;
        if (!phasable.origins$canPhase()) {
            player.sendMessage(Text.translatable("message.cuteorigins.cannot_phase"), true);
            return;
        }

        boolean phasing = !phasable.origins$isPhasing();
        phasable.origins$setAndSyncPhasing(phasing);

        if (phasing) {
            player.addVelocity(0d, -0.1d, 0d);
            player.velocityModified = true;
        }
    }

    public static Effect fromJson(JsonObject object) {
        return new TogglePhasingEffect();
    }
}
