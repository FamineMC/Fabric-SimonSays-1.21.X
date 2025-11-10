package net.famine.simonsays.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.famine.simonsays.component.LifeTimerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class LifeTimerStartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Saw").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(1, 10000))
                        .executes(commandContext -> {
                            ServerCommandSource source = commandContext.getSource();
                            Entity entity = source.getEntity();
                            int lifetimer = IntegerArgumentType.getInteger(commandContext, "amount");
                            if (entity != null) {
                                LifeTimerComponent timerComponent = LifeTimerComponent.KEY.get(entity);
                                timerComponent.setLifeTimer(lifetimer);
                                entity.sendMessage(Text.literal("Set life timer to " + lifetimer));
                            }
                            return 0;
                        })
                )
        );
    }
}
