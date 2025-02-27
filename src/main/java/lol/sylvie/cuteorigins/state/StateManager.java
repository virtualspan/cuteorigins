package lol.sylvie.cuteorigins.state;

import lol.sylvie.cuteorigins.CuteOrigins;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.HashMap;
import java.util.UUID;

public class StateManager extends PersistentState {
    public HashMap<UUID, PlayerData> players = new HashMap<>();

    private static final Type<StateManager> TYPE = new Type<>(
            StateManager::new,
            StateManager::createFromNbt,
            null
    );

    public static StateManager createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        StateManager state = new StateManager();

        NbtCompound playersNbt = tag.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = new PlayerData();
            NbtCompound playerNbt = playersNbt.getCompound(key);

            NbtElement originElement = playerNbt.get("origin");
            if (originElement != null) playerData.selectedOrigin = Identifier.of(originElement.asString());

            NbtList shulkerElement = playerNbt.getList("shulker_inventory", NbtList.COMPOUND_TYPE);
            if (shulkerElement != null) playerData.shulkerInventory.readNbtList(shulkerElement, registryLookup);

            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });

        return state;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound playersNbt = new NbtCompound();

        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();

            if (playerData.selectedOrigin != null) playerNbt.putString("origin", playerData.selectedOrigin.toString());
            playerNbt.put("shulker_inventory", playerData.shulkerInventory.toNbtList(registryLookup));

            playersNbt.put(uuid.toString(), playerNbt);
        });

        nbt.put("players", playersNbt);
        return nbt;
    }

    public static StateManager getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getOverworld().getPersistentStateManager();
        StateManager state = persistentStateManager.getOrCreate(TYPE, CuteOrigins.MOD_ID);

        state.markDirty();

        return state;
    }

    public static PlayerData getPlayerState(LivingEntity player) {
        if (player.getServer() == null)
            throw new IllegalStateException("Tried to get the player state of a non-server entity.");
        if (!(player instanceof PlayerEntity))
            throw new IllegalStateException("Non-player entities shouldn't have data!");
        StateManager serverState = getServerState(player.getServer());
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());
    }
}
