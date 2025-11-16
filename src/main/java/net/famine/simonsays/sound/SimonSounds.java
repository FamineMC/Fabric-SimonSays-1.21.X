package net.famine.simonsays.sound;

import net.famine.simonsays.SimonSays;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SimonSounds {


    //Knocks
    public static SoundEvent KNOCK = registerSoundEvent("knock");


    //Ambient Sounds
    public static SoundEvent HEARTBEAT_AMBIENT = registerSoundEvent("heartbeat_ambient");
    public static SoundEvent HORROR_AMBIENT_1 = registerSoundEvent("horror_ambient_1");
    public static SoundEvent HORROR_AMBIENT_2 = registerSoundEvent("horror_ambient_2");

    //Task Sounds
    public static SoundEvent TASK_ASSIGN = registerSoundEvent("task_assign");
    public static SoundEvent TASK_COMPLETE = registerSoundEvent("task_complete");
    public static SoundEvent TASK_FAIL = registerSoundEvent("task_fail");

    //Jumpscare Sound
    public static SoundEvent JUMPSCARE_SOUND = registerSoundEvent("jumpscare_sound");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(SimonSays.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds(){
        SimonSays.LOGGER.info("Registering Sounds for " + SimonSays.MOD_ID);
    }
}
