package net.famine.simonsays.mixin;


import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
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
        if(living instanceof PlayerEntity player){
            LifeTimerComponent lifeTimerComponent = LifeTimerComponent.KEY.get(player);
            TaskTimerComponent taskTimerComponent = TaskTimerComponent.KEY.get(player);

            if(effect.equals(potionEffectTaskEffect.get(taskTimerComponent.randomStatus)) && taskTimerComponent.taskCurrentlyActive && taskTimerComponent.taskFourActive){
                taskTimerComponent.taskCurrentlyActive = false;
                taskTimerComponent.taskOneActive = false;
                taskTimerComponent.taskTwoActive = false;
                taskTimerComponent.taskThreeActive = false;
                taskTimerComponent.taskFourActive = false;
                player.sendMessage(Text.literal("previous time: " + lifeTimerComponent.lifeTimer));
                lifeTimerComponent.addToSyncedLifeTimer(player, 300);
                player.sendMessage(Text.literal("current time: " + lifeTimerComponent.lifeTimer));
                player.sendMessage(Text.literal("Task Complete!"));
                player.clearStatusEffects();
            }
        }

    }
}
