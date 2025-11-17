package net.famine.simonsays.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.famine.simonsays.SimonSays;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public record KeybindFailTaskPayload() implements CustomPayload {
    public static final Id<KeybindFailTaskPayload> ID = new Id<>(SimonSays.id("key_hit_bad"));
    public static final PacketCodec<RegistryByteBuf, KeybindFailTaskPayload> CODEC = PacketCodec.unit(new KeybindFailTaskPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<KeybindFailTaskPayload> {
        @Override
        public void receive(@NotNull KeybindFailTaskPayload payload, ServerPlayNetworking.@NotNull Context context) {
            var player = context.player();
            if (player == null) return;
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);
            if (taskTimerComponent.taskCurrentlyActive) {
                if (taskTimerComponent.taskSixActive) {
                    taskTimerComponent.setTaskCurrentlyActive(false);
                    taskTimerComponent.setTaskOneActive(false);
                    taskTimerComponent.setTaskTwoActive(false);
                    taskTimerComponent.setTaskThreeActive(false);
                    taskTimerComponent.setTaskFourActive(false);
                    taskTimerComponent.setTaskFiveActive(false);
                    taskTimerComponent.setTaskSixActive(false);
                    taskTimerComponent.setRandomTask(0);
                    taskTimerComponent.taskTimer = 100;
                    lifeTimerComponent.subtractFromSyncedLifeTimer(player, 1200);
                    long subtractSeconds = 1200 / 20;
                    long subtractMinutes = subtractSeconds / 60;
                    long remainingSubtractSeconds = subtractSeconds % 60;
                    player.sendMessage(Text.literal(String.format("%d:%02d", subtractMinutes, remainingSubtractSeconds).formatted(Formatting.RED))
                            .append(Text.literal(" removed from your life. Do better next time.").formatted(Formatting.RED)), true);
                    player.playSoundToPlayer(SimonSounds.TASK_FAIL, SoundCategory.PLAYERS, 1f, 1f);
                    taskTimerComponent.sync();
                }
            }
        }
    }
}
