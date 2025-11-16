package net.famine.simonsays.component;

import net.famine.simonsays.SimonSays;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
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
    public int deathTimer = 200;
    public boolean youreDead = false;

    public boolean hasStartedTimer = false;

    public LifeTimerComponent(PlayerEntity notSimon) {
        this.notSimon = notSimon;
    }

    public int getLifeTimer() {
        return this.lifeTimer;
    }

    public void setLifeTimer(int lifeTimer) {
        this.lifeTimer = lifeTimer;
    }

    public void setDeathTimer(int deathTimer){
        this.deathTimer = deathTimer;
    }

    public void sync() {
        KEY.sync(this.notSimon);
    }


    @Override
    public void tick() {
        TimeBetweenTasksComponent betweenTasksComponent = TimeBetweenTasksComponent.KEY.get(notSimon);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(notSimon);
        if (!(this.lifeTimer <= 0)){
            this.lifeTimer--;
            sync();
            if (this.notSimon.isDead()) {
                this.lifeTimer = getLifeTimer();
                sync();
            }
            if (this.notSimon.getRandom().nextInt(200) == 0) {
                this.notSimon.playSoundToPlayer(SimonSounds.HEARTBEAT_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
            }
        }
        if (this.lifeTimer <= 0 && hasStartedTimer){
            youreDead = true;
            hasStartedTimer = false;
            betweenTasksComponent.setBufferTimer(0);
            taskTimerComponent.setTaskTimer(0);
            this.sync();
        }
        if (youreDead){

            deathTimer--;
            this.sync();
            if(deathTimer <= 0){
                youreDead = false;
                this.sync();
                if(!notSimon.getWorld().isClient()){
                    notSimon.playSoundToPlayer(SimonSounds.JUMPSCARE_SOUND, SoundCategory.PLAYERS, 1f, 1f);
                    notSimon.kill();
                    if (notSimon instanceof ServerPlayerEntity playerEntity){
                        playerEntity.changeGameMode(GameMode.SPECTATOR);
                    }
                }

            }
        }
    }

    public void addToSyncedLifeTimer(PlayerEntity entity, int add) {
        MinecraftServer server = entity.getWorld().getServer();
        if (server == null) return;
        for (var players : server.getPlayerManager().getPlayerList()) {
            LifeTimerComponent timerComponent = LifeTimerComponent.KEY.get(players);
            timerComponent.setLifeTimer(timerComponent.lifeTimer + add);
            sync();
        }
    }

    public void subtractFromSyncedLifeTimer(PlayerEntity entity, int subtract) {
        MinecraftServer server = entity.getWorld().getServer();
        if (server == null) return;
        for (var players : server.getPlayerManager().getPlayerList()) {
            LifeTimerComponent timerComponent = LifeTimerComponent.KEY.get(players);
            timerComponent.setLifeTimer(timerComponent.lifeTimer - subtract);
            sync();
        }
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.lifeTimer = nbtCompound.getInt("lifetimercount");
        this.deathTimer = nbtCompound.getInt("deathtimercount");
        this.hasStartedTimer = nbtCompound.getBoolean("hasStartedTimer");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("lifetimercount", this.lifeTimer);
        nbtCompound.putInt("deathtimercount", this.deathTimer);
        nbtCompound.putBoolean("hasStartedTimer", this.hasStartedTimer);
    }
}
