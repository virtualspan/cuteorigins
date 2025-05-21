package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.condition.Condition;
import lol.sylvie.cuteorigins.power.effect.Effect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FireEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("fire");

    private final Condition condition;

    protected FireEffect(Condition condition) {
        super(IDENTIFIER, false);
        this.condition = condition;
    }

    @Override
    public void onTick(ServerPlayerEntity player) {
        if (this.condition.test(player)) {
            player.setOnFireForTicks(40);
        }
    }

    public static Effect fromJson(JsonObject object) {
        return new FireEffect(Condition.fromJson(object.getAsJsonObject("condition")));
    }
}
