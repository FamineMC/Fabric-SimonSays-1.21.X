package net.famine.simonsays.component;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;


public class SimonEvents {

    public Item itemSelection;

    public static void obtainItemTask(PlayerEntity player){
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
        TimeBetweenTasksComponent timeBetweenTasksComponent = TimeBetweenTasksComponent.KEY.get(player);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);
        timeBetweenTasksComponent.assignedTaskTime = 2400;

        List<Item> items = new ArrayList<>();

        items.add(Items.STICK);
        items.add(Items.WOODEN_SWORD);
        items.add(Items.OAK_FENCE);
        items.add(Items.COBBLESTONE_WALL);


        Item randomItem = items.get(player.getRandom().nextInt(items.size()));


        player.sendMessage(Text.literal("item is: " + randomItem));



        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            ItemStack stack = playerEntity.getStackInHand(hand);
            if(stack.isOf(randomItem) && taskTimerComponent.taskCurrentlyActive){
                taskTimerComponent.taskCurrentlyActive = false;
                playerEntity.getInventory().clear();
                player.sendMessage(Text.literal("previous time: " + lifeTimerComponent.lifeTimer));
                lifeTimerComponent.lifeTimer += 300;
                player.sendMessage(Text.literal("current time: " + lifeTimerComponent.lifeTimer));
                player.sendMessage(Text.literal("Task Complete!"));

                stack = null;





            }
            return TypedActionResult.pass(stack);
        });
    }
    public static void killMobTask(PlayerEntity player){

    }
    public static void consumeItemTask(PlayerEntity player) {

        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
        TimeBetweenTasksComponent timeBetweenTasksComponent = TimeBetweenTasksComponent.KEY.get(player);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);

        timeBetweenTasksComponent.assignedTaskTime = 3600;

        List<Item> items = new ArrayList<>();

        items.add(Items.COOKED_BEEF);
        items.add(Items.PORKCHOP);
        items.add(Items.COOKED_CHICKEN);
        items.add(Items.COOKED_MUTTON);

        Random random = player.getRandom();
        int randomIndex = random.nextInt(items.size());
        Item randomItem = items.get(randomIndex);

        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            ItemStack stack = playerEntity.getStackInHand(hand);
            if(stack.isOf(randomItem) && taskTimerComponent.taskCurrentlyActive){
                taskTimerComponent.taskCurrentlyActive = false;
                playerEntity.getStackInHand(hand).decrement(1);
                lifeTimerComponent.lifeTimer += 600;
                player.sendMessage(Text.literal("Task Complete!"));

            }
            return TypedActionResult.pass(stack);
        });

    }
}
