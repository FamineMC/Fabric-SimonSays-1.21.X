package net.famine.simonsays.mixin;

import net.famine.simonsays.SimonSays;
import net.famine.simonsays.client.TaskStuffRenderer;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Nullable protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Unique
    private static final Identifier JUMPSCARE_OVERLAY = SimonSays.id("textures/misc/jumpscareoverlay.png");
    @Unique
    private static final Identifier NORMAL_OVERLAY = SimonSays.id("textures/misc/jumpscarepeekoverlay.png");

    @Inject(method = "renderMiscOverlays", at = @At("TAIL"))
    private void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return;
        PlayerEntity player = this.getCameraPlayer();
        if (player == null) return;
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
        TaskStuffRenderer.renderBar(context, world, this.getCameraPlayer());
        if (lifeTimerComponent.youreDead) {
            this.renderOverlay(context, NORMAL_OVERLAY, 100f);
        }
        if (lifeTimerComponent.deathTimer <= 0 && player.isDead() && lifeTimerComponent.doJumpscareScreen) {
            lifeTimerComponent.sync();
            this.renderOverlay(context, JUMPSCARE_OVERLAY, 100f);
            if(player.isAlive()){
                this.renderOverlay(context, JUMPSCARE_OVERLAY, 0f);
                lifeTimerComponent.doJumpscareScreen = false;
            }
        }
    }
}