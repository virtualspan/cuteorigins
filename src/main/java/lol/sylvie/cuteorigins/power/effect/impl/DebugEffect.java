package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.effect.Effect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class DebugEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("debug");
    protected DebugEffect() {
        super(IDENTIFIER, true);
    }

    @Override
    public void onRespawn(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Respawn event"));
    }

    @Override
    public ActionResult onAttack(PlayerEntity player, Entity target) {
        player.sendMessage(Text.literal("Attack event"), false);
        return ActionResult.PASS;
    }

    @Override
    public void onChosen(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Chosen event"));
    }

    @Override
    public void onRemoved(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Removed event"));
    }

    @Override
    public void onAction(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Action event"));
    }

    @Override
    public void onTick(ServerPlayerEntity player) {
        //player.sendMessage(Text.literal("Tick event"));
    }

    public static Effect fromJson(JsonObject object) {
        return new DebugEffect();
    }
}
