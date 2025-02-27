package lol.sylvie.cuteorigins.mixininterfaces;

// A lot of the logic for phasing is inspired by OriginsPaper
// https://github.com/Dueris/OriginsPaper/blob/origin/origins/src/main/java/io/github/dueris/originspaper/mixin/ServerPlayerMixin.java#L133
public interface Phasable {
    boolean origins$isPhasing();
    boolean origins$canPhase();

    void origins$setPhasing(boolean value);
}
