package net.famine.simonsays.mixin;


import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnKilledCriterion.class)
public class OnKilledCriterionMixin {
    @Inject(at = @At("TAIL"), method = "trigger", cancellable = true)
    private void init(ServerPlayerEntity player, Entity entity, DamageSource killingDamage, CallbackInfo ci){
        LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
        TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);

        if(entity.getType() == taskTimerComponent.randomEntity && killingDamage.isDirect() && taskTimerComponent.taskCurrentlyActive && taskTimerComponent.taskThreeActive){
            taskTimerComponent.taskCurrentlyActive = false;
            taskTimerComponent.taskOneActive = false;
            taskTimerComponent.taskTwoActive = false;
            taskTimerComponent.taskThreeActive = false;
            taskTimerComponent.taskFourActive = false;
            taskTimerComponent.taskFiveActive = false;
            taskTimerComponent.taskSixActive = false;
            taskTimerComponent.randomTask = 0;
            lifeTimerComponent.addToSyncedLifeTimer(player, 400);
            long addSeconds = 400 / 20;
            long addMinutes = addSeconds / 60;
            long remainingAddSeconds = addSeconds % 60;
            player.sendMessage(Text.literal(String.format("%d:%02d", addMinutes, remainingAddSeconds))
                    .append(Text.literal(" added to your life!")), true);
            player.playSoundToPlayer(SimonSounds.TASK_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);

        }
    }
}
