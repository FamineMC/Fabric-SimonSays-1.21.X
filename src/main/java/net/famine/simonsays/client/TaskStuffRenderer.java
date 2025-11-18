package net.famine.simonsays.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.famine.simonsays.SimonSays;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TaskStuffRenderer {
    private static final int WIDTH = 182;
    private static final int HEIGHT = 12;

    public static void renderBar(@NotNull DrawContext context, @NotNull ClientWorld world, @Nullable PlayerEntity player) {
        if (player == null) return;
        var x = context.getScaledWindowWidth() / 2;
        var x2 = context.getScaledWindowWidth() / 2 - 30;
        var y = 12;
        var i = 0;
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);
        long lifeTimerSeconds = lifeTimerComponent.lifeTimer / 20;
        long lifeTimerMinutes = lifeTimerSeconds / 60;
        long remainingLifeTimerSeconds = lifeTimerSeconds % 60;
        var lifeTimer = Text.literal("Time Left: ").append(String.format("%d:%02d", lifeTimerMinutes, remainingLifeTimerSeconds));
        long taskTimerSeconds = taskTimerComponent.taskTimer / 20;
        long taskTimerMinutes = taskTimerSeconds / 60;
        long remainingTaskTimerSeconds = taskTimerSeconds % 60;
        var taskTimer = Text.literal("Complete the task in: ").append(String.format("%d:%02d", taskTimerMinutes, remainingTaskTimerSeconds));
        var renderer = MinecraftClient.getInstance().textRenderer;
        var halfOfLifeText = (MinecraftClient.getInstance().textRenderer.getWidth(lifeTimer) / 2);
        var halfOfTaskText = (MinecraftClient.getInstance().textRenderer.getWidth(taskTimer) / 2);
        var textX = x - halfOfLifeText;
        var textX2 = x - halfOfTaskText;

        if (lifeTimerComponent.lifeTimer > 0 && !lifeTimerComponent.youreDead) {
            RenderSystem.enableBlend();
            context.drawTextWithShadow(renderer, lifeTimer, textX, y + 14 + HEIGHT / 2 - renderer.fontHeight / 2, Colors.GREEN);
            if(taskTimerComponent.taskCurrentlyActive){

                context.drawTextWithShadow(renderer, taskTimer, textX2, y + 30 + HEIGHT / 2 - renderer.fontHeight / 2, Colors.LIGHT_GRAY);
            }

            RenderSystem.disableBlend();
        }
    }
}