package net.famine.simonsays.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.famine.simonsays.component.LifeTimerComponent;
import net.famine.simonsays.component.TimeBetweenTasksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class LifeTimerStartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("Saw").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4))
                .then(CommandManager.argument("amount", IntegerArgumentType.integer(1, 24000))
                        .then(CommandManager.argument("commandUserExcluded", BoolArgumentType.bool())
                                .executes(commandContext -> {
                                ServerCommandSource source = commandContext.getSource();
                                MinecraftServer server = source.getServer();
                                Entity entity = source.getEntity();
                                boolean excluded = BoolArgumentType.getBool(commandContext, "commandUserExcluded");
                                int lifetimer = IntegerArgumentType.getInteger(commandContext, "amount");
                                if (excluded) {
                                    for (var players : server.getPlayerManager().getPlayerList()) {
                                        if (players != entity) {
                                            LifeTimerComponent timerComponent = LifeTimerComponent.KEY.get(players);
                                            TimeBetweenTasksComponent betweenTasksComponent = TimeBetweenTasksComponent.KEY.get(players);
                                            timerComponent.setLifeTimer(lifetimer);
                                            timerComponent.hasStartedTimer = true;
                                            betweenTasksComponent.taskHasBeenAssigned = false;
                                        }
                                    }
                                    if (entity != null) {
                                        entity.sendMessage(Text.literal("Set global life timer to " + lifetimer + " without including self"));
                                    }
                                } else {
                                    for (var players : server.getPlayerManager().getPlayerList()){
                                        LifeTimerComponent timerComponent = LifeTimerComponent.KEY.get(players);
                                        TimeBetweenTasksComponent betweenTasksComponent = TimeBetweenTasksComponent.KEY.get(players);
                                        timerComponent.setLifeTimer(lifetimer);
                                        timerComponent.hasStartedTimer = true;
                                        betweenTasksComponent.taskHasBeenAssigned = false;
                                    }
                                    if (entity != null) {
                                        entity.sendMessage(Text.literal("Set global life timer to " + lifetimer));
                                    }
                                }
                                return 0;
                                }))
                )
        );
    }
}
