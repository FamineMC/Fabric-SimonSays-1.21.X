package net.famine.simonsays.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.famine.simonsays.SimonSays;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public record KeybindTaskPayload(boolean keyPressed) implements CustomPayload {
    public static final Id<KeybindTaskPayload> ID = new Id<>(SimonSays.id("jump"));
    public static final PacketCodec<RegistryByteBuf, KeybindTaskPayload> CODEC = PacketCodec.tuple(PacketCodecs.BOOL, KeybindTaskPayload::keyPressed, KeybindTaskPayload::new);

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
