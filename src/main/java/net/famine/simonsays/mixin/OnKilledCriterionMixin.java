package net.famine.simonsays.mixin;


import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TaskTimerComponent;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
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
            player.sendMessage(Text.literal("previous time: " + lifeTimerComponent.lifeTimer));
            lifeTimerComponent.addToSyncedLifeTimer(player, 300);
            player.sendMessage(Text.literal("current time: " + lifeTimerComponent.lifeTimer));
            player.sendMessage(Text.literal("Task Complete!"));

        }
    }
}
