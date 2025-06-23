package lol.sylvie.cuteorigins.power.effect.impl;

import com.google.gson.JsonObject;
import lol.sylvie.cuteorigins.CuteOrigins;
import lol.sylvie.cuteorigins.power.condition.Condition;
import lol.sylvie.cuteorigins.power.effect.Effect;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SleepingConditionEffect extends Effect {
    public static final Identifier IDENTIFIER = CuteOrigins.identifier("sleeping_condition");

    private final Condition condition;
    private final Text message;

    protected SleepingConditionEffect(Condition condition, Text message) {
        super(IDENTIFIER, false);
        this.condition = condition;
        this.message = message;
    }

    public Condition getCondition() {
        return condition;
    }

    public Text getMessage() {
        return message;
    }

    public static Effect fromJson(JsonObject object) {
        return new SleepingConditionEffect(Condition.fromJson(object.getAsJsonObject("condition")), Text.translatable(object.get("message").getAsString()));
    }
}
