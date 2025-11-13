package net.famine.simonsays.mixin;

import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryOwner.class)
public class InventoryOwnerMixin {
    @Inject(at = @At("HEAD"), method = "pickUpItem", cancellable = true)
    private static void init(MobEntity entity, InventoryOwner inventoryOwner, ItemEntity item, CallbackInfo ci){
        ItemStack stack = item.getStack();


        if(stack.isOf(randomItem) && taskTimerComponent.taskCurrentlyActive){
            taskTimerComponent.taskCurrentlyActive = false;
            playerEntity.getInventory().clear();
            player.sendMessage(Text.literal("previous time: " + lifeTimerComponent.lifeTimer));
            lifeTimerComponent.lifeTimer += 300;
            player.sendMessage(Text.literal("current time: " + lifeTimerComponent.lifeTimer));
            player.sendMessage(Text.literal("Task Complete!"));


        }
    }
}
