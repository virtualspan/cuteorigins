package lol.sylvie.cuteorigins.power;

import net.minecraft.util.Identifier;

import java.util.HashMap;

public class PowerRegistry {
    public HashMap<Identifier, Power> powers = new HashMap<>();

    public void addPower(Power power) {
        this.powers.put(power.getIdentifier(), power);
    }

    public Power getPower(Identifier identifier) {
        return this.powers.get(identifier);
    }

    public boolean hasPower(Identifier identifier) {
        return this.powers.containsKey(identifier);
    }

    public void clearRegistry() {
        powers.clear();
    }
}
