package net.famine.simonsays;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.famine.simonsays.command.LifeTimerStartCommand;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.component.TimeBetweenTasksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimonSays implements ModInitializer, EntityComponentInitializer {
	public static final String MOD_ID = "simonsays";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            LifeTimerStartCommand.register(commandDispatcher);
        });
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            ItemStack stack = playerEntity.getStackInHand(hand);
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(playerEntity);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(playerEntity);
            if(stack.isOf(taskTimerComponent.randomConsumeItem) && taskTimerComponent.taskCurrentlyActive && taskTimerComponent.taskTwoActive){
                taskTimerComponent.taskCurrentlyActive = false;
                taskTimerComponent.taskOneActive = false;
                taskTimerComponent.taskTwoActive = false;
                taskTimerComponent.taskThreeActive = false;
                taskTimerComponent.taskFourActive = false;
                taskTimerComponent.taskFiveActive = false;
                taskTimerComponent.taskSixActive = false;
                playerEntity.getStackInHand(hand).decrement(1);
                playerEntity.sendMessage(Text.literal("previous time: " + lifeTimerComponent.lifeTimer));
                lifeTimerComponent.addToSyncedLifeTimer(playerEntity, 600);
                playerEntity.sendMessage(Text.literal("current time: " + lifeTimerComponent.lifeTimer));
                playerEntity.sendMessage(Text.literal("Task Complete!"));


            }
            return TypedActionResult.pass(stack);
        });
	}

	public static @NotNull Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, LifeTimerComponent.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(LifeTimerComponent::new);
		registry.beginRegistration(PlayerEntity.class, TimeBetweenTasksComponent.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(TimeBetweenTasksComponent::new);
		registry.beginRegistration(PlayerEntity.class, TaskTimerComponent.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(TaskTimerComponent::new);
    }
}