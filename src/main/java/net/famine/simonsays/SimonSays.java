package net.famine.simonsays;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimonSays implements ModInitializer {
	public static final String MOD_ID = "simonsays";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

	}

	public static @NotNull Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}