package net.famine.simonsays.mixin;


import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.famine.simonsays.sound.SimonSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.famine.simonsays.component.TaskTimerComponent.potionEffectTaskEffect;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At("TAIL"), method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", cancellable = true)
    void init(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir){
        LivingEntity living = (LivingEntity)(Object) this;
        if(living instanceof PlayerEntity playerEntity){
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(playerEntity);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(playerEntity);

            if(effect.equals(potionEffectTaskEffect.get(taskTimerComponent.randomStatus)) && taskTimerComponent.taskCurrentlyActive && taskTimerComponent.taskFourActive){
                taskTimerComponent.setTaskCurrentlyActive(false);
                taskTimerComponent.setTaskOneActive(false);
                taskTimerComponent.setTaskTwoActive(false);
                taskTimerComponent.setTaskThreeActive(false);
                taskTimerComponent.setTaskFourActive(false);
                taskTimerComponent.setTaskFiveActive(false);
                taskTimerComponent.setTaskSixActive(false);
                taskTimerComponent.setRandomTask(0);
                lifeTimerComponent.addToSyncedLifeTimer(playerEntity, 700);
                long addSeconds = 700 / 20;
                long addMinutes = addSeconds / 60;
                long remainingAddSeconds = addSeconds % 60;
                playerEntity.sendMessage(Text.literal(String.format("%d:%02d", addMinutes, remainingAddSeconds))
                        .append(Text.literal(" added to your life!")), true);
                playerEntity.clearStatusEffects();
                playerEntity.playSoundToPlayer(SimonSounds.TASK_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);
            }
            if(!effect.equals(potionEffectTaskEffect.get(taskTimerComponent.randomStatus)) && taskTimerComponent.taskCurrentlyActive && taskTimerComponent.taskFourActive){
                taskTimerComponent.setTaskCurrentlyActive(false);
                taskTimerComponent.setTaskOneActive(false);
                taskTimerComponent.setTaskTwoActive(false);
                taskTimerComponent.setTaskThreeActive(false);
                taskTimerComponent.setTaskFourActive(false);
                taskTimerComponent.setTaskFiveActive(false);
                taskTimerComponent.setTaskSixActive(false);
                taskTimerComponent.setRandomTask(0);
                lifeTimerComponent.subtractFromSyncedLifeTimer(playerEntity, 900);

                long subtractSeconds = 900 / 20;
                long subtractMinutes = subtractSeconds / 60;
                long remainingSubtractSeconds = subtractSeconds % 60;
                playerEntity.sendMessage(Text.literal(String.format("%d:%02d", subtractMinutes, remainingSubtractSeconds))
                        .append(Text.literal(" removed from your life. Do better next time.")), true);
                playerEntity.playSoundToPlayer(SimonSounds.TASK_FAIL, SoundCategory.PLAYERS, 1f, 1f);
                playerEntity.clearStatusEffects();
            }
        }

    }
}
