package lol.sylvie.cuteorigins.power.effect;

import com.google.gson.JsonObject;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public abstract class Effect {
    private final Identifier identifier;
    private final boolean hasAction;

    protected Effect(Identifier identifier, boolean hasAction) {
        this.identifier = identifier;
        this.hasAction = hasAction;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public static Effect fromJson(JsonObject object) throws IllegalAccessException {
        throw new IllegalAccessException("Do not call Effect::fromJson, call a subclasses fromJson. (you probably forgot to specify one!)");
    }

    public void onRespawn(ServerPlayerEntity player) {}
    public void onTick(ServerPlayerEntity player) {}
    public ActionResult onAttack(PlayerEntity player, Entity target) {
        return ActionResult.PASS;
    }

    public boolean hasAction() {
        return hasAction;
    }

    public void onAction(ServerPlayerEntity player) {
        if (!hasAction) throw new RuntimeException("Attempted to use action when power type has no action.");
    }

    public void onChosen(ServerPlayerEntity player) {}
    public void onRemoved(ServerPlayerEntity player) {}
}
