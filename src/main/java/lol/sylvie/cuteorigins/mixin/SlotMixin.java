package lol.sylvie.cuteorigins.mixin;

import lol.sylvie.cuteorigins.item.ModItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {
    @Shadow @Final public Inventory inventory;

    @Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
    public void cuteorigins$noKeybindInsertion(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (stack.isOf(ModItems.KEYBIND_ITEM) && !(inventory instanceof PlayerInventory)) cir.setReturnValue(false);
    }
}
