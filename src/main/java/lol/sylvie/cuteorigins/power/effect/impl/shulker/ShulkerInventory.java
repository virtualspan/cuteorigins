package lol.sylvie.cuteorigins.power.effect.impl.shulker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.dynamic.Codecs;

import java.util.Arrays;
import java.util.List;

public class ShulkerInventory extends SimpleInventory {
    private static final int SIZE = 9;

    public static final Codec<ShulkerInventory> CODEC = ItemStack.CODEC.sizeLimitedListOf(SIZE).xmap(
            ShulkerInventory::new,
            inventory -> inventory.heldStacks.stream().toList()
    );


    private static ItemStack[] defaultedItems(List<ItemStack> items) {
        ItemStack[] array = new ItemStack[SIZE];
        Arrays.fill(array, ItemStack.EMPTY);
        for (int i = 0; i < items.size(); i++) {
            array[i] = items.get(i);
        }
        return array;
    }

    public ShulkerInventory() {
        super(SIZE);
    }

    public ShulkerInventory(List<ItemStack> items) {
        // i hate this
        super(defaultedItems(items));
    }
}
