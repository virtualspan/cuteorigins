package lol.sylvie.cuteorigins.event;

import lol.sylvie.cuteorigins.command.OriginCommand;
import lol.sylvie.cuteorigins.gui.OriginGui;
import lol.sylvie.cuteorigins.item.ModComponents;
import lol.sylvie.cuteorigins.item.ModItems;
import lol.sylvie.cuteorigins.item.impl.KeybindItem;
import lol.sylvie.cuteorigins.origin.Origin;
import lol.sylvie.cuteorigins.power.effect.Effect;
import lol.sylvie.cuteorigins.power.effect.impl.CannotUseEffect;
import lol.sylvie.cuteorigins.power.effect.impl.SleepingConditionEffect;
import lol.sylvie.cuteorigins.state.StateManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

public class EventRegistry {
    public static boolean cannotUse(Item item, PlayerEntity user) {
        Origin origin = StateManager.getPlayerState(user).getOrigin();
        if (origin == null) return false;
        for (Effect effect : origin.getEffectsOfType(CannotUseEffect.class)) {
            CannotUseEffect cannotUseEffect = (CannotUseEffect) effect;
            if (!cannotUseEffect.isAllowedToUse(item)) {
                return true;
            }
        }
        return false;
    }

    public static Text canSleep(PlayerEntity player) {
        Origin origin = StateManager.getPlayerState(player).getOrigin();
        if (origin == null) return null;
        for (Effect effect : origin.getEffectsOfType(SleepingConditionEffect.class)) {
            SleepingConditionEffect sleepingConditionEffect = (SleepingConditionEffect) effect;
            if (!sleepingConditionEffect.getCondition().test(player)) {
                return sleepingConditionEffect.getMessage();
            }
        }
        return null;
    }

    public static void register() {
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            Origin origin = StateManager.getPlayerState(newPlayer).getOrigin();
            if (origin != null) origin.onRespawn(newPlayer);
        });

        ServerTickEvents.START_WORLD_TICK.register(world -> {
            world.getPlayers().forEach((player) -> {
                Origin origin = StateManager.getPlayerState(player).getOrigin();
                if (origin != null) origin.onTick(player);
            });

            MinecraftServer server = world.getServer();
            ArrayList<ItemStack> buffer = new ArrayList<>();
            KeybindItem.UPDATE_MAP.forEach((stack, timestamp) -> {
                if (server.getTicks() >= timestamp) {
                    stack.set(ModComponents.ON_COOLDOWN, false);
                    buffer.add(stack);
                }
            });
            buffer.forEach(stack -> KeybindItem.UPDATE_MAP.remove(stack));
        });

        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof ServerPlayerEntity player) {
                Origin origin = StateManager.getPlayerState(player).getOrigin();
                if (origin == null) {
                    OriginGui.openPicker(player);
                }
            }

            if (entity instanceof ItemEntity item && item.getStack().isOf(ModItems.KEYBIND_ITEM)) {
                entity.kill(world);
            }
        });

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            Origin origin = StateManager.getPlayerState(player).getOrigin();
            if (origin != null) return origin.onAttack(player, entity);
            return ActionResult.PASS;
        });

        EntitySleepEvents.ALLOW_SLEEPING.register((playerEntity, blockPos) -> {
            Text message = canSleep(playerEntity);
            if (message != null) {
                playerEntity.sendMessage(message, true);
                return PlayerEntity.SleepFailureReason.NOT_POSSIBLE_HERE;
            }
            return null;
        });

        UseItemCallback.EVENT.register((playerEntity, world, hand) -> cannotUse(playerEntity.getStackInHand(hand).getItem(), playerEntity) ? ActionResult.CONSUME : ActionResult.PASS);
        UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> cannotUse(playerEntity.getStackInHand(hand).getItem(), playerEntity) ? ActionResult.CONSUME : ActionResult.PASS);
        UseEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> cannotUse(playerEntity.getStackInHand(hand).getItem(), playerEntity) ? ActionResult.CONSUME : ActionResult.PASS);

        CommandRegistrationCallback.EVENT.register(OriginCommand::register);
    }
}
