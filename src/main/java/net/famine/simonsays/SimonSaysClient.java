package net.famine.simonsays;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import javax.swing.text.JTextComponent;


public class SimonSaysClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            var player = client.player;
            if (player == null) return;
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);
            if (taskTimerComponent.randomKeybind != null) {
                if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), taskTimerComponent.randomKeybind)
                        && taskTimerComponent.taskCurrentlyActive) {
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
        });
    }
}
