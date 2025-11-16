package net.famine.simonsays.mixin;

import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
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
            taskTimerComponent.taskFiveActive = false;
            taskTimerComponent.taskSixActive = false;
            taskTimerComponent.randomTask = 0;
            taskTimerComponent.taskTimer = 100;
            taskTimerComponent.sync();
            lifeTimerComponent.addToSyncedLifeTimer(player, 400);
            long addSeconds = 400 / 20;
            long addMinutes = addSeconds / 60;
            long remainingAddSeconds = addSeconds % 60;
            player.sendMessage(Text.literal(String.format("%d:%02d", addMinutes, remainingAddSeconds))
                    .append(Text.literal(" added to your life!")), true);
            player.playSoundToPlayer(SimonSounds.TASK_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);

        }
    }
}
