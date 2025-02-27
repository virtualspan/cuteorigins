package lol.sylvie.cuteorigins.mixin;

import lol.sylvie.cuteorigins.origin.Origin;
import lol.sylvie.cuteorigins.power.effect.impl.ScareCreeperEffect;
import lol.sylvie.cuteorigins.state.StateManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {
    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private boolean scaresCreepers(LivingEntity living) {
        Origin origin = StateManager.getPlayerState(living).getOrigin();
        if (origin == null) return false;
        return origin.getFirstEffect(ScareCreeperEffect.class) != null;
    }

    @Inject(at = @At("TAIL"), method = "initGoals")
    private void addGoals(CallbackInfo ci) {
        Goal goal = new FleeEntityGoal<>(this, PlayerEntity.class, this::scaresCreepers, 6.0F, 1.0D, 1.2D, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
        this.goalSelector.add(3, goal);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 8), method = "initGoals")
    private void redirectTargetGoal(GoalSelector instance, int priority, Goal goal) {
        Goal newGoal = new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, (target, world) -> !scaresCreepers(target));
        goalSelector.add(priority, newGoal);
    }
}