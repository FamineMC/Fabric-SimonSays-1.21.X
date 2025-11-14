package net.famine.simonsays.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {

    @Inject(at = @At("TAIL"), method = "onKeyPressed", cancellable = true)
    private static void init(InputUtil.Key key, CallbackInfo ci){
        if (MinecraftClient.getInstance().getCameraEntity() instanceof PlayerEntity player) {
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);
            if (key.getCode() == taskTimerComponent.randomKeybind && taskTimerComponent.taskCurrentlyActive) {
                 if (taskTimerComponent.taskFiveActive) {
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
    }
}
