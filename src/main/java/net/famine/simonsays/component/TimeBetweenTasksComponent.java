package net.famine.simonsays.component;

import net.famine.simonsays.SimonSays;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.Random;

public class TimeBetweenTasksComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<TimeBetweenTasksComponent> KEY = ComponentRegistry.getOrCreate(SimonSays.id("timebetweentasks"), TimeBetweenTasksComponent.class);

    private final PlayerEntity notSimon;

    public int bufferTimer = 0;

    public int min = 400;
    public int max = 800;

    public int assignedTaskTime;

    public TimeBetweenTasksComponent(PlayerEntity notSimon) {
        this.notSimon = notSimon;
    }

    public int getBufferTimer() {
        return this.bufferTimer;
    }

    public void setBufferTimer(int bufferTimer) {
        this.bufferTimer = bufferTimer;
    }

    public boolean taskHasBeenAssigned = false;



    @Override
    public void tick() {
        LifeTimerComponent timerComponent = LifeTimerComponent.KEY.get(notSimon);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(notSimon);
        if (!(this.bufferTimer <= 0) && !taskTimerComponent.taskCurrentlyActive && timerComponent.hasStartedTimer){
            this.bufferTimer--;
            //notSimon.sendMessage(Text.literal("buffer timer: " + this.BufferTimer), true);

        }


        if (this.bufferTimer == 0){
            Random random = new Random();
            int r = random.nextInt(min, max);
            setBufferTimer(r);


            //notSimon.sendMessage(Text.literal("Buffer Timer Set To: " + r));

            if (timerComponent.hasStartedTimer){

                taskHasBeenAssigned = true;
            }

        }


    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.bufferTimer = nbtCompound.getInt("timebetweentaskscount");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("timebetweentaskscount", this.bufferTimer);
    }
}
