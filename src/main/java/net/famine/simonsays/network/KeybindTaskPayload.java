package net.famine.simonsays.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.famine.simonsays.SimonSays;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public record KeybindTaskPayload() implements CustomPayload {
    public static final Id<KeybindTaskPayload> ID = new Id<>(SimonSays.id("key_hit_good"));
    public static final PacketCodec<RegistryByteBuf, KeybindTaskPayload> CODEC = PacketCodec.unit(new KeybindTaskPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<KeybindTaskPayload> {
        @Override
        public void receive(@NotNull KeybindTaskPayload payload, ServerPlayNetworking.@NotNull Context context) {
            var player = context.player();
            if (player == null) return;
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);
            if (taskTimerComponent.taskCurrentlyActive) {
                if (taskTimerComponent.taskFiveActive) {
                    taskTimerComponent.setTaskCurrentlyActive(false);
                    taskTimerComponent.setTaskOneActive(false);
                    taskTimerComponent.setTaskTwoActive(false);
                    taskTimerComponent.setTaskThreeActive(false);
                    taskTimerComponent.setTaskFourActive(false);
                    taskTimerComponent.setTaskFiveActive(false);
                    taskTimerComponent.setTaskSixActive(false);
                    taskTimerComponent.setRandomTask(0);
                    taskTimerComponent.taskTimer = 100;
                    lifeTimerComponent.addToSyncedLifeTimer(player, 300);
                    long addSeconds = 300 / 20;
                    long addMinutes = addSeconds / 60;
                    long remainingAddSeconds = addSeconds % 60;
                    player.sendMessage(Text.literal(String.format("%d:%02d", addMinutes, remainingAddSeconds))
                            .append(Text.literal(" added to your life!")), true);
                    player.playSoundToPlayer(SimonSounds.TASK_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);
                    taskTimerComponent.sync();
                }
            }
        }
    }
}
