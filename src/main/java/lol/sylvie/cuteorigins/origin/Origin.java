package lol.sylvie.cuteorigins.origin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.item.impl.KeybindItem;
import lol.sylvie.cuteorigins.power.Power;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.util.JsonHelper;
import lol.sylvie.cuteorigins.util.OriginRegistries;
import lol.sylvie.cuteorigins.util.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public record Origin(Identifier identifier, Item icon, List<Power> powers) {
    public Text getName() {
        return TextUtil.getIdentifierText(this.identifier, "origin", "name");
    }

    public Text getDescription() {
        return TextUtil.getIdentifierText(this.identifier, "origin", "description");
    }

    public List<Power> getDisplayPowers() {
        return powers.stream().filter(Power::isVisible).sorted(Comparator.comparing(Power::isNegative)).toList();
    }

    public boolean hasPower(Power power) {
        return this.powers.contains(power);
    }

    public List<ItemStack> getKeybinds() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (Power power : this.powers()) {
            if (power.getEffect().hasAction()) {
                items.add(KeybindItem.getKeybind(power.getIdentifier()));
            }
        }
        return items;
    }

    public Effect getFirstEffect(Class<? extends Effect> clazz) {
        List<Effect> effects = getEffectsOfType(clazz);
        if (effects.isEmpty()) return null;
        return effects.getFirst();
    }

    public List<Effect> getEffectsOfType(Class<? extends Effect> clazz) {
        return this.powers().stream()
                .map(Power::getEffect)
                .filter(effect -> effect.getClass().equals(clazz))
                .toList();
    }

    public static Origin fromJson(Identifier identifier, JsonObject object) {
        Identifier itemId = JsonHelper.jsonStringToIdentifier(object.get("icon"));
        Item item = Registries.ITEM.get(itemId).asItem();

        List<JsonElement> powerNames = object.getAsJsonArray("powers").asList();
        List<Power> powerList = powerNames.stream()
                .map(JsonHelper::jsonStringToIdentifier)
                .filter(power -> {
                    if (!OriginRegistries.POWER_REGISTRY.hasPower(power)) {
                        CuteOrigins.LOGGER.warn("Origin {} tried to use power {} doesn't exist.", identifier, power);
                        return false;
                    }
                    return true;
                })
                .map(OriginRegistries.POWER_REGISTRY::getPower).toList();

        return new Origin(identifier, item, powerList);
    }

    private void forEachEffect(Consumer<Effect> runnable) {
        this.powers.forEach(power -> runnable.accept(power.getEffect()));
    }

    public void onRespawn(ServerPlayerEntity player) {
        forEachEffect(effect -> effect.onRespawn(player));
    }

    public void onTick(ServerPlayerEntity player) {
        forEachEffect(effect -> effect.onTick(player));
    }

    public ActionResult onAttack(PlayerEntity player, Entity target) {
        for (Power power : this.powers) {
            ActionResult thisResult = power.getEffect().onAttack(player, target);
            if (thisResult != ActionResult.PASS) {
                return thisResult;
            }
        }
        return ActionResult.PASS;
    }

    public void onChosen(ServerPlayerEntity player) {
        forEachEffect(effect -> effect.onChosen(player));
    }

    public void onRemoved(ServerPlayerEntity player) {
        forEachEffect(effect -> effect.onRemoved(player));
    }
}
