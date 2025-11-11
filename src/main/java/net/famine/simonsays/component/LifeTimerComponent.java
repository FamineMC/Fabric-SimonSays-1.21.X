package net.famine.simonsays.component;

import net.famine.simonsays.SimonSays;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class LifeTimerComponent implements AutoSyncedComponent, CommonTickingComponent {

    public static final ComponentKey<LifeTimerComponent> KEY = ComponentRegistry.getOrCreate(SimonSays.id("lifetimer"), LifeTimerComponent.class);

    private final PlayerEntity notSimon;

    private int LifeTimer = 0;

    public LifeTimerComponent(PlayerEntity notSimon) {
        this.notSimon = notSimon;
    }

    public int getLifeTimer() {
        return this.LifeTimer;
    }

    public void setLifeTimer(int lifeTimer) {
        this.LifeTimer = lifeTimer;
    }

    public boolean hasStartedTimer = false;

    @Override
    public void tick() {
        if (!(this.LifeTimer <= 0)){
            this.LifeTimer--;
            notSimon.sendMessage(Text.literal("" + this.LifeTimer), true);
        }
        if (this.LifeTimer == 0 && hasStartedTimer){
            notSimon.kill();
            hasStartedTimer = false;
            if (notSimon instanceof ServerPlayerEntity playerEntity){
                playerEntity.changeGameMode(GameMode.SPECTATOR);
            }

        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.LifeTimer = nbtCompound.getInt("lifetimercount");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("lifetimercount", this.LifeTimer);
    }
}
