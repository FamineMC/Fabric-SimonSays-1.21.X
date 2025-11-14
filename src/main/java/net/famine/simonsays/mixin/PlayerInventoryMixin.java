package net.famine.simonsays.mixin;

import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Inject(at = @At("TAIL"), method = "addStack(ILnet/minecraft/item/ItemStack;)I", cancellable = true)
    private void init(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir){
        var self = (PlayerInventory) (Object) (this);
        PlayerEntity player = self.player;
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);

        if(stack.isOf(taskTimerComponent.randomPickupItem) && taskTimerComponent.taskCurrentlyActive && taskTimerComponent.taskOneActive){
            taskTimerComponent.taskCurrentlyActive = false;
            taskTimerComponent.taskOneActive = false;
            taskTimerComponent.taskTwoActive = false;
            taskTimerComponent.taskThreeActive = false;
            taskTimerComponent.taskFourActive = false;
            player.sendMessage(Text.literal("previous time: " + lifeTimerComponent.lifeTimer));
            lifeTimerComponent.addToSyncedLifeTimer(player, 300);
            player.sendMessage(Text.literal("current time: " + lifeTimerComponent.lifeTimer));
            player.sendMessage(Text.literal("Task Complete!"));

        }
    }
}
