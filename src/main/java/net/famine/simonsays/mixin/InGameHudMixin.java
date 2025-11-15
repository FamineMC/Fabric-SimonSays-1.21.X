package net.famine.simonsays.mixin;

import net.famine.simonsays.client.TaskStuffRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Nullable protected abstract PlayerEntity getCameraPlayer();

    @Inject(method = "renderMiscOverlays", at = @At("HEAD"))
    private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return;
        TaskStuffRenderer.renderBar(context, world, this.getCameraPlayer());
    }
}