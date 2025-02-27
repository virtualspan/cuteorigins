package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.condition.Condition;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class PotionImmunityEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("potion_immunity");

    private final Identifier effect;

    protected PotionImmunityEffect(Identifier effect) {
        super(IDENTIFIER, false);
        this.effect = effect;
    }

    @Override
    public void onTick(ServerPlayerEntity player) {
        Registry<StatusEffect> statusEffects = player.getRegistryManager().getOrThrow(RegistryKeys.STATUS_EFFECT);
        RegistryEntry<StatusEffect> statusEffect = statusEffects.getEntry(effect).orElseThrow();

        if (player.hasStatusEffect(statusEffect)) {
            player.removeStatusEffect(statusEffect);
        }
    }

    public static Effect fromJson(JsonObject object) {
        return new PotionImmunityEffect(JsonHelper.jsonStringToIdentifier(object.get("status_effect")));
    }
}
