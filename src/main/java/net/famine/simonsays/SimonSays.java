package net.famine.simonsays;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.famine.simonsays.command.LifeTimerStartCommand;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.component.TimeBetweenTasksComponent;
import net.famine.simonsays.network.KeybindFailTaskPayload;
import net.famine.simonsays.network.KeybindTaskPayload;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.InputEvent;

public class SimonSays implements ModInitializer, EntityComponentInitializer {
	public static final String MOD_ID = "simonsays";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        SimonSounds.registerSounds();
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            LifeTimerStartCommand.register(commandDispatcher);
        });
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            ItemStack stack = playerEntity.getStackInHand(hand);
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(playerEntity);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(playerEntity);
            if(stack.isOf(taskTimerComponent.randomConsumeItem) && taskTimerComponent.taskCurrentlyActive && taskTimerComponent.taskTwoActive){
                taskTimerComponent.setTaskCurrentlyActive(false);
                taskTimerComponent.setTaskOneActive(false);
                taskTimerComponent.setTaskTwoActive(false);
                taskTimerComponent.setTaskThreeActive(false);
                taskTimerComponent.setTaskFourActive(false);
                taskTimerComponent.setTaskFiveActive(false);
                taskTimerComponent.setTaskSixActive(false);
                taskTimerComponent.setRandomTask(0);
                playerEntity.getStackInHand(hand).decrement(1);
                lifeTimerComponent.addToSyncedLifeTimer(playerEntity, 600);
                long addSeconds = 600 / 20;
                long addMinutes = addSeconds / 60;
                long remainingAddSeconds = addSeconds % 60;
                playerEntity.sendMessage(Text.literal(String.format("%d:%02d", addMinutes, remainingAddSeconds))
                        .append(Text.literal(" added to your life!")), true);
                playerEntity.playSoundToPlayer(SimonSounds.TASK_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);
            }
            return TypedActionResult.pass(stack);
        });
        PayloadTypeRegistry.playC2S().register(KeybindTaskPayload.ID, KeybindTaskPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(KeybindTaskPayload.ID, new KeybindTaskPayload.Receiver());
        PayloadTypeRegistry.playC2S().register(KeybindFailTaskPayload.ID, KeybindFailTaskPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(KeybindFailTaskPayload.ID, new KeybindFailTaskPayload.Receiver());
        SimonSounds.registerSounds();
	}

	public static @NotNull Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, LifeTimerComponent.KEY).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(LifeTimerComponent::new);
		registry.beginRegistration(PlayerEntity.class, TimeBetweenTasksComponent.KEY).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(TimeBetweenTasksComponent::new);
		registry.beginRegistration(PlayerEntity.class, TaskTimerComponent.KEY).respawnStrategy(RespawnCopyStrategy.CHARACTER).end(TaskTimerComponent::new);
    }
}