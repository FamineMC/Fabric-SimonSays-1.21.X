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

    public int lifeTimer = 0;

    public LifeTimerComponent(PlayerEntity notSimon) {
        this.notSimon = notSimon;
    }

    public int getLifeTimer() {
        return this.lifeTimer;
    }

    public void setLifeTimer(int lifeTimer) {
        this.lifeTimer = lifeTimer;
    }

    public boolean hasStartedTimer = false;

    @Override
    public void tick() {
        TimeBetweenTasksComponent betweenTasksComponent = TimeBetweenTasksComponent.KEY.get(notSimon);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(notSimon);
        if (!(this.lifeTimer <= 0)){
            this.lifeTimer--;
            notSimon.sendMessage(Text.literal("" + this.lifeTimer), true);
        }
        if (this.lifeTimer == 0 && hasStartedTimer){
            notSimon.kill();
            hasStartedTimer = false;
            betweenTasksComponent.setBufferTimer(0);
            taskTimerComponent.setTaskTimer(0);
            if (notSimon instanceof ServerPlayerEntity playerEntity){
                playerEntity.changeGameMode(GameMode.SPECTATOR);
            }

        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.lifeTimer = nbtCompound.getInt("lifetimercount");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("lifetimercount", this.lifeTimer);
    }
}
