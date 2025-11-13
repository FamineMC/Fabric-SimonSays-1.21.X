package net.famine.simonsays;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.famine.simonsays.command.LifeTimerStartCommand;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.SimonEvents;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.component.TimeBetweenTasksComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
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