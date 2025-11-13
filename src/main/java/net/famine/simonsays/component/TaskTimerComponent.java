package net.famine.simonsays.component;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.famine.simonsays.SimonSays;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class TaskTimerComponent implements AutoSyncedComponent, CommonTickingComponent {

    public static final ComponentKey<TaskTimerComponent> KEY = ComponentRegistry.getOrCreate(SimonSays.id("tasktimer"), TaskTimerComponent.class);

    private final PlayerEntity notSimon;

    private int taskTimer = 0;

    public boolean taskCurrentlyActive;

    public TaskTimerComponent(PlayerEntity notSimon) {
        this.notSimon = notSimon;
    }

    public int getTaskTimer() {
        return this.taskTimer;
    }

    public void setTaskTimer(int taskTimer) {
        this.taskTimer = taskTimer;
    }

    public Item item = Items.STICK;

    public void sync() {
        KEY.sync(this.notSimon);
    }

    @Override
    public void tick() {
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(notSimon);
        TimeBetweenTasksComponent timeBetweenTasksComponent = TimeBetweenTasksComponent.KEY.get(notSimon);

        if (!(this.taskTimer <= 0) && taskCurrentlyActive && lifeTimerComponent.hasStartedTimer){
            this.taskTimer--;
            notSimon.sendMessage(Text.literal("task timer " + this.taskTimer), true);
        }
        if (timeBetweenTasksComponent.taskHasBeenAssigned){

            notSimon.sendMessage(Text.literal("Task Assigned!"));
            timeBetweenTasksComponent.taskHasBeenAssigned = false;
            SimonEvents.obtainItemTask(notSimon);
            setTaskTimer(timeBetweenTasksComponent.assignedTaskTime);
            notSimon.sendMessage(Text.literal("task timer set to " + timeBetweenTasksComponent.assignedTaskTime));
            taskCurrentlyActive = true;

        }

        if (lifeTimerComponent.hasStartedTimer && this.taskTimer == 0 && taskCurrentlyActive){
            taskCurrentlyActive = false;
            notSimon.sendMessage(Text.literal("No Current Tasks."));
        }


    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.taskTimer = nbtCompound.getInt("tasktimercount");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("tasktimercount", this.taskTimer);
    }
}
