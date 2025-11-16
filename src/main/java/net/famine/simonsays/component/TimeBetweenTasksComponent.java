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

    public void setTaskHasBeenAssigned(boolean taskHasBeenAssigned) {
        this.taskHasBeenAssigned = taskHasBeenAssigned;
        sync();
    }

    public void setAssignedTaskTime(int assignedTaskTime) {
        this.assignedTaskTime = assignedTaskTime;
        sync();
    }

    public void sync() {
        KEY.sync(this.notSimon);
    }

    @Override
    public void tick() {
        LifeTimerComponent timerComponent = LifeTimerComponent.KEY.get(notSimon);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(notSimon);
        if (timerComponent.hasStartedTimer && this.bufferTimer > 0) {
            if (!taskTimerComponent.taskCurrentlyActive){
                this.bufferTimer--;
                //notSimon.sendMessage(Text.literal("buffer timer: " + this.bufferTimer), true);

            } else {
                this.bufferTimer = getBufferTimer();
            }
        }

        if (this.bufferTimer == 0){
            Random random = new Random();
            int r = random.nextInt(min, max);
            setBufferTimer(r);


            //notSimon.sendMessage(Text.literal("Buffer Timer Set To: " + r));

            if(timerComponent.hasStartedTimer){
                if(!notSimon.getWorld().isClient()){
                    taskHasBeenAssigned = true;
                    sync();
                }

            }


        }


    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.bufferTimer = nbtCompound.getInt("timebetweentaskscount");
        this.assignedTaskTime = nbtCompound.getInt("assignedTaskTime");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("timebetweentaskscount", this.bufferTimer);
        nbtCompound.putInt("assignedTaskTime", this.assignedTaskTime);
    }
}
