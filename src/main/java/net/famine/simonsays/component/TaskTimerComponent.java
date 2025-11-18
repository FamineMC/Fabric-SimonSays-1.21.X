package net.famine.simonsays.component;

import net.famine.simonsays.SimonSays;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import org.lwjgl.glfw.GLFW;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TaskTimerComponent implements AutoSyncedComponent, CommonTickingComponent {

    public static final ComponentKey<TaskTimerComponent> KEY = ComponentRegistry.getOrCreate(SimonSays.id("tasktimer"), TaskTimerComponent.class);

    private final PlayerEntity notSimon;

    public int taskTimer = 100;

    public boolean taskCurrentlyActive = false;

    public boolean taskOneActive = false;
    public boolean taskTwoActive = false;
    public boolean taskThreeActive = false;
    public boolean taskFourActive = false;
    public boolean taskFiveActive = false;
    public boolean taskSixActive = false;

    public List<Item> itemPickupTaskItems = new ArrayList<>();
    public List<Item> consumeItemTaskItems = new ArrayList<>();
    public List<EntityType<?>> killEntityTaskEntity = new ArrayList<>();
    public static List<RegistryEntry<StatusEffect>> potionEffectTaskEffect = new ArrayList<>();
    public static List<Integer> keybindTasksKeybinds = new ArrayList<>();

    public Item randomPickupItem;
    public Item randomConsumeItem;
    public EntityType<?> randomEntity;
    public int randomStatus = 0;
    public Integer randomKeybind = 0;
    public int randomTask = 0;

    public TaskTimerComponent(PlayerEntity notSimon) {
        this.notSimon = notSimon;
        itemPickupTaskItems.add(Items.STICK);
        itemPickupTaskItems.add(Items.WOODEN_SWORD);
        itemPickupTaskItems.add(Items.ACACIA_FENCE);
        itemPickupTaskItems.add(Items.COBBLESTONE_WALL);
        itemPickupTaskItems.add(Items.LADDER);
        itemPickupTaskItems.add(Items.STONE_SHOVEL);
        itemPickupTaskItems.add(Items.CHEST);
        itemPickupTaskItems.add(Items.COBBLESTONE_SLAB);
        itemPickupTaskItems.add(Items.LEVER);

        consumeItemTaskItems.add(Items.COOKED_BEEF);
        consumeItemTaskItems.add(Items.COOKED_PORKCHOP);
        consumeItemTaskItems.add(Items.COOKED_CHICKEN);
        consumeItemTaskItems.add(Items.COOKED_MUTTON);
        consumeItemTaskItems.add(Items.COOKED_COD);
        consumeItemTaskItems.add(Items.COOKED_SALMON);
        consumeItemTaskItems.add(Items.COOKED_RABBIT);

        killEntityTaskEntity.add(EntityType.PIG);
        killEntityTaskEntity.add(EntityType.COW);
        killEntityTaskEntity.add(EntityType.ZOMBIE);
        killEntityTaskEntity.add(EntityType.CHICKEN);
        killEntityTaskEntity.add(EntityType.SPIDER);
        killEntityTaskEntity.add(EntityType.SLIME);
        killEntityTaskEntity.add(EntityType.RABBIT);
        killEntityTaskEntity.add(EntityType.SILVERFISH);

        potionEffectTaskEffect.add(StatusEffects.FIRE_RESISTANCE);
        potionEffectTaskEffect.add(StatusEffects.STRENGTH);
        potionEffectTaskEffect.add(StatusEffects.REGENERATION);
        potionEffectTaskEffect.add(StatusEffects.POISON);
        potionEffectTaskEffect.add(StatusEffects.NIGHT_VISION);

        keybindTasksKeybinds.add(InputUtil.GLFW_KEY_SPACE);
        keybindTasksKeybinds.add(InputUtil.GLFW_KEY_LEFT_SHIFT);
        keybindTasksKeybinds.add(InputUtil.GLFW_KEY_W);
        keybindTasksKeybinds.add(InputUtil.GLFW_KEY_A);
        keybindTasksKeybinds.add(InputUtil.GLFW_KEY_S);
        keybindTasksKeybinds.add(InputUtil.GLFW_KEY_D);
    }

    public int getTaskTimer() {
        return this.taskTimer;
    }

    public void setTaskTimer(int taskTimer) {
        this.taskTimer = taskTimer;
    }


    public List<EntityType<?>> getKillEntityTaskEntity() {
        return killEntityTaskEntity;
    }

    public List<Item> getConsumeItemTaskItems() {
        return consumeItemTaskItems;
    }

    public List<Item> getItemPickupTaskItems() {
        return itemPickupTaskItems;
    }

    public static List<Integer> getKeybindTasksKeybinds() {
        return keybindTasksKeybinds;
    }

    public static List<RegistryEntry<StatusEffect>> getPotionEffectTaskEffect() {
        return potionEffectTaskEffect;
    }

    public Item getRandomPickupItem() {
        return randomPickupItem;
    }

    public Integer getRandomKeybind() {
        return randomKeybind;
    }

    public Item getRandomConsumeItem() {
        return randomConsumeItem;
    }

    public EntityType<?> getRandomEntity() {
        return randomEntity;
    }

    public int getRandomStatus() {
        return randomStatus;
    }

    public void sync() {
        KEY.sync(this.notSimon);
    }

    public void setTaskOneActive(boolean taskOneActive) {
        this.taskOneActive = taskOneActive;
        sync();
    }

    public void setTaskTwoActive(boolean taskTwoActive) {
        this.taskTwoActive = taskTwoActive;
        sync();
    }

    public void setTaskThreeActive(boolean taskThreeActive) {
        this.taskThreeActive = taskThreeActive;
        sync();
    }

    public void setTaskFourActive(boolean taskFourActive) {
        this.taskFourActive = taskFourActive;
        sync();
    }

    public void setTaskFiveActive(boolean taskFiveActive) {
        this.taskFiveActive = taskFiveActive;
        sync();
    }

    public void setTaskSixActive(boolean taskSixActive) {
        this.taskSixActive = taskSixActive;
        sync();
    }

    public void setTaskCurrentlyActive(boolean taskCurrentlyActive) {
        this.taskCurrentlyActive = taskCurrentlyActive;
        sync();
    }

    public void setRandomTask(int randomTask) {
        this.randomTask = randomTask;
        sync();
    }

    @Override
    public void tick() {
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(notSimon);
        TimeBetweenTasksComponent timeBetweenTasksComponent = TimeBetweenTasksComponent.KEY.get(notSimon);


        if (lifeTimerComponent.hasStartedTimer && taskCurrentlyActive &&  this.taskTimer > 0){
            this.taskTimer--;
            sync();
            if (this.notSimon.isDead()) {
                this.taskTimer = getTaskTimer();
                this.itemPickupTaskItems = getItemPickupTaskItems();
                this.consumeItemTaskItems = getConsumeItemTaskItems();
                this.killEntityTaskEntity = getKillEntityTaskEntity();
                this.randomConsumeItem = getRandomConsumeItem();
                this.randomPickupItem = getRandomPickupItem();
                this.randomEntity = getRandomEntity();
                this.randomStatus = getRandomStatus();
                this.randomKeybind = getRandomKeybind();
                sync();
            }
        }
        if (timeBetweenTasksComponent.taskHasBeenAssigned){
            timeBetweenTasksComponent.setTaskHasBeenAssigned(false);
            int randomTask = this.notSimon.getWorld().getRandom().nextInt(6);
            //notSimon.sendMessage(Text.literal("Task Assigned!"));
            notSimon.playSoundToPlayer(SimonSounds.TASK_ASSIGN, SoundCategory.PLAYERS, 1f, 1f);
            if (!taskCurrentlyActive) {
            switch (randomTask) {
                case 0 -> {
                    timeBetweenTasksComponent.setAssignedTaskTime(1200);
                    Item randomPickup = itemPickupTaskItems.get(notSimon.getRandom().nextInt(itemPickupTaskItems.size()));

                    this.randomPickupItem = randomPickup;
                    sync();
                    this.randomTask = 1;


                    this.notSimon.sendMessage(Text.literal("Pick Up A ").append(this.randomPickupItem.getName()));

                    this.taskOneActive = true;
                    this.taskTwoActive = false;
                    this.taskThreeActive = false;
                    this.taskFourActive = false;
                    this.taskFiveActive = false;
                    this.taskSixActive = false;
                    this.taskCurrentlyActive = true;
                    sync();
                }
                case 1 -> {
                    timeBetweenTasksComponent.setAssignedTaskTime(1600);
                    Item randomConsume = consumeItemTaskItems.get(notSimon.getRandom().nextInt(consumeItemTaskItems.size()));

                    this.randomConsumeItem = randomConsume;
                    sync();
                    this.randomTask = 2;





                    this.notSimon.sendMessage(Text.literal("Eat A ").append(this.randomConsumeItem.getName()));

                    this.taskOneActive = false;
                    this.taskTwoActive = true;
                    this.taskThreeActive = false;
                    this.taskFourActive = false;
                    this.taskFiveActive = false;
                    this.taskSixActive = false;
                    this.taskCurrentlyActive = true;
                    sync();
                }
                case 2 ->{
                    timeBetweenTasksComponent.setAssignedTaskTime(1200);
                    EntityType<?> randomEntityEntity = killEntityTaskEntity.get(notSimon.getRandom().nextInt(killEntityTaskEntity.size()));

                    this.randomEntity = randomEntityEntity;
                    sync();
                    this.randomTask = 3;




                    this.notSimon.sendMessage(Text.literal("Kill The ").append(this.randomEntity.getName()));

                    this.taskOneActive = false;
                    this.taskTwoActive = false;
                    this.taskThreeActive = true;
                    this.taskFourActive = false;
                    this.taskFiveActive = false;
                    this.taskSixActive = false;
                    this.taskCurrentlyActive = true;
                    sync();
                }
                case 3 ->{
                    timeBetweenTasksComponent.setAssignedTaskTime(1600);
                    Random random = new Random();
                    this.randomStatus = random.nextInt(potionEffectTaskEffect.size());
                    sync();
                    this.randomTask = 4;

                    this.notSimon.sendMessage(Text.literal("Drink A Potion Of ").append(Text.translatable(potionEffectTaskEffect.get(randomStatus).value().getTranslationKey())));
                    this.taskOneActive = false;
                    this.taskTwoActive = false;
                    this.taskThreeActive = false;
                    this.taskFourActive = true;
                    this.taskFiveActive = false;
                    this.taskSixActive = false;
                    this.taskCurrentlyActive = true;
                    sync();
                }
                case 4 ->{
                    timeBetweenTasksComponent.setAssignedTaskTime(200);
                    this.randomKeybind = keybindTasksKeybinds.get(notSimon.getRandom().nextInt(keybindTasksKeybinds.size()));
                    sync();
                    this.randomTask = 5;


                    this.notSimon.sendMessage(Text.literal("Press The ")
                            .append(InputUtil.fromKeyCode(this.randomKeybind, 0).getLocalizedText())
                            .append(Text.literal(" Key")));
                    this.taskOneActive = false;
                    this.taskTwoActive = false;
                    this.taskThreeActive = false;
                    this.taskFourActive = false;
                    this.taskFiveActive = true;
                    this.taskSixActive = false;
                    this.taskCurrentlyActive = true;
                    sync();
                }
                case 5 ->{
                    timeBetweenTasksComponent.setAssignedTaskTime(200);
                    this.randomKeybind = keybindTasksKeybinds.get(notSimon.getRandom().nextInt(keybindTasksKeybinds.size()));
                    sync();
                    this.randomTask = 6;

                   this.notSimon.sendMessage(Text.literal("Don't Press The: ")
                            .append(InputUtil.fromKeyCode(this.randomKeybind, 0).getLocalizedText())
                           .append(Text.literal(" Key")));
                    this.taskOneActive = false;
                    this.taskTwoActive = false;
                    this.taskThreeActive = false;
                    this.taskFourActive = false;
                    this.taskFiveActive = false;
                    this.taskSixActive = true;
                    this.taskCurrentlyActive = true;
                    sync();
                }
            }
            setTaskTimer(timeBetweenTasksComponent.assignedTaskTime);
}
        }



        if (lifeTimerComponent.hasStartedTimer && this.taskTimer == 0){
            if (this.taskCurrentlyActive) {
                if (this.taskSixActive) {
                    if(!notSimon.getWorld().isClient()){
                        this.taskCurrentlyActive = false;
                        this.taskSixActive = false;
                        this.randomTask = 0;
                        this.taskTimer = 100;
                        lifeTimerComponent.addToSyncedLifeTimer(this.notSimon, 300);
                        long addSeconds = 300 / 20;
                        long addMinutes = addSeconds / 60;
                        long remainingAddSeconds = addSeconds % 60;
                        this.notSimon.sendMessage(Text.literal(String.format("%d:%02d", addMinutes, remainingAddSeconds).formatted(Formatting.GREEN))
                                .append(Text.literal(" added to your life!").formatted(Formatting.GREEN)), true);
                        this.notSimon.playSoundToPlayer(SimonSounds.TASK_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);
                        this.sync();
                    }

                } else {
                    if(!notSimon.getWorld().isClient()){
                        this.taskCurrentlyActive = false;
                        this.taskOneActive = false;
                        this.taskTwoActive = false;
                        this.taskThreeActive = false;
                        this.taskFourActive = false;
                        this.taskFiveActive = false;
                        this.taskSixActive = false;
                        this.randomTask = 0;
                        this.taskTimer = 100;
                        lifeTimerComponent.subtractFromSyncedLifeTimer(this.notSimon, 1200);
                        long subtractSeconds = 1200 / 20;
                        long subtractMinutes = subtractSeconds / 60;
                        long remainingSubtractSeconds = subtractSeconds % 60;
                        this.notSimon.sendMessage(Text.literal(String.format("%d:%02d", subtractMinutes, remainingSubtractSeconds).formatted(Formatting.RED))
                                .append(Text.literal(" removed from your life. Do better next time.").formatted(Formatting.RED)), true);
                        this.notSimon.playSoundToPlayer(SimonSounds.TASK_FAIL, SoundCategory.PLAYERS, 1f, 1f);
                        sync();
                    }

                }
                sync();
            }
        }
    }





    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.taskTimer = nbtCompound.getInt("tasktimercount");
        this.randomTask = nbtCompound.getInt("randomTask");
        this.randomKeybind = nbtCompound.getInt("randomKeybind");
        this.taskCurrentlyActive = nbtCompound.getBoolean("taskCurrentlyActive");
        this.taskOneActive = nbtCompound.getBoolean("taskOneActive");
        this.taskTwoActive = nbtCompound.getBoolean("taskTwoActive");
        this.taskThreeActive = nbtCompound.getBoolean("taskThreeActive");
        this.taskFourActive = nbtCompound.getBoolean("taskFourActive");
        this.taskFiveActive = nbtCompound.getBoolean("taskFiveActive");
        this.taskSixActive = nbtCompound.getBoolean("taskSixActive");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("tasktimercount", this.taskTimer);
        nbtCompound.putInt("randomTask", this.randomTask);
        nbtCompound.putInt("randomKeybind", this.randomKeybind);
        nbtCompound.putBoolean("taskCurrentlyActive", this.taskCurrentlyActive);
        nbtCompound.putBoolean("taskOneActive", this.taskOneActive);
        nbtCompound.putBoolean("taskTwoActive", this.taskTwoActive);
        nbtCompound.putBoolean("taskThreeActive", this.taskThreeActive);
        nbtCompound.putBoolean("taskFourActive", this.taskFourActive);
        nbtCompound.putBoolean("taskFiveActive", this.taskFiveActive);
        nbtCompound.putBoolean("taskSixActive", this.taskSixActive);
    }
}
