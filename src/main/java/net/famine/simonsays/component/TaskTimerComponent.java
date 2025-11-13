package net.famine.simonsays.component;

import net.famine.simonsays.SimonSays;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class TaskTimerComponent implements AutoSyncedComponent, CommonTickingComponent {

    public static final ComponentKey<TaskTimerComponent> KEY = ComponentRegistry.getOrCreate(SimonSays.id("tasktimer"), TaskTimerComponent.class);

    private final PlayerEntity notSimon;

    public int taskTimer = 0;

    public boolean taskCurrentlyActive;

    public boolean taskOneActive;
    public boolean taskTwoActive;
    public boolean taskThreeActive;
    public boolean taskFourActive;
    public boolean taskFiveActive;
    public boolean taskSixActive;



    public List<Item> itemPickupTaskItems = new ArrayList<>();
    public List<Item> consumeItemTaskItems = new ArrayList<>();
    public List<Entity> killEntityTaskEntity = new ArrayList<>();

    public Item randomPickupItem;
    public Item randomConsumeItem;
    public Entity randomEntity;

    public TaskTimerComponent(PlayerEntity notSimon) {
        this.notSimon = notSimon;
        itemPickupTaskItems.add(Items.STICK);
        itemPickupTaskItems.add(Items.WOODEN_SWORD);
        itemPickupTaskItems.add(Items.OAK_FENCE);
        itemPickupTaskItems.add(Items.COBBLESTONE_WALL);

        consumeItemTaskItems.add(Items.COOKED_BEEF);
        consumeItemTaskItems.add(Items.COOKED_PORKCHOP);
        consumeItemTaskItems.add(Items.COOKED_CHICKEN);
        consumeItemTaskItems.add(Items.COOKED_MUTTON);


        //** these didn't work**
        //killEntityTaskEntity.add(EntityType.PIG);
        //killEntityTaskEntity.add(EntityType.COW);
        //killEntityTaskEntity.add(EntityType.ZOMBIE);
        //killEntityTaskEntity.add(EntityType.CHICKEN);

    }

    public int getTaskTimer() {
        return this.taskTimer;
    }

    public void setTaskTimer(int taskTimer) {
        this.taskTimer = taskTimer;
    }

    public void sync() {
        KEY.sync(this.notSimon);
        KEY.sync(this.itemPickupTaskItems);
        KEY.sync(this.randomPickupItem);
        KEY.sync(this.randomEntity);
    }

    @Override
    public void tick() {
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(notSimon);
        TimeBetweenTasksComponent timeBetweenTasksComponent = TimeBetweenTasksComponent.KEY.get(notSimon);
        int randomTask = this.notSimon.getRandom().nextInt(2);

        if (!(this.taskTimer <= 0) && taskCurrentlyActive && lifeTimerComponent.hasStartedTimer){
            this.taskTimer--;
            notSimon.sendMessage(Text.literal("task timer " + this.taskTimer), true);
        }
        if (timeBetweenTasksComponent.taskHasBeenAssigned){

            notSimon.sendMessage(Text.literal("Task Assigned!"));
            timeBetweenTasksComponent.taskHasBeenAssigned = false;
            switch (randomTask) {
                case 0 -> {
                    Item randomPickup = itemPickupTaskItems.get(notSimon.getRandom().nextInt(itemPickupTaskItems.size()));

                    this.randomPickupItem = randomPickup;

                    timeBetweenTasksComponent.assignedTaskTime = 2400;

                    notSimon.sendMessage(Text.literal("Pick up the: " + randomPickup));

                    taskOneActive = true;
                    taskTwoActive = false;

                }
                case 1 -> {
                    Item randomConsume = consumeItemTaskItems.get(notSimon.getRandom().nextInt(consumeItemTaskItems.size()));

                    this.randomConsumeItem = randomConsume;

                    timeBetweenTasksComponent.assignedTaskTime = 3600;

                    notSimon.sendMessage(Text.literal("Consume the: " + randomConsume));

                    taskOneActive = false;
                    taskTwoActive = true;
                }
                case 2 ->{
                    Entity randomEntityEntity = killEntityTaskEntity.get(notSimon.getRandom().nextInt(killEntityTaskEntity.size()));

                    this.randomEntity = randomEntityEntity;

                    timeBetweenTasksComponent.assignedTaskTime = 3600;

                    notSimon.sendMessage(Text.literal("Kill the: " + randomEntityEntity));


                }
            }
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
