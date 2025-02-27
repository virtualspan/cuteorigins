package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ActionVelocityEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("action_velocity");

    private final Vec3d velocity;

    protected ActionVelocityEffect(Vec3d velocity) {
        super(IDENTIFIER, true);
        this.velocity = velocity;
    }

    @Override
    public void onAction(ServerPlayerEntity player) {
        player.addVelocity(velocity);
        player.velocityModified = true;
    }

    public static Effect fromJson(JsonObject object) {
        return new ActionVelocityEffect(JsonHelper.jsonListToVec3d(object.getAsJsonArray("velocity")));
    }
}
