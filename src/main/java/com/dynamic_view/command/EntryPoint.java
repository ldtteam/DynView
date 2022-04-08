package com.dynamic_view.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

/**
 * Entry point to commands.
 */
public class EntryPoint
{

    private EntryPoint()
    {
        // Intentionally left empty
    }

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher)
    {
        CommandTree performantCommands = new CommandTree("dynview")
          .addNode(new CommandSetViewDistance().build())
          .addNode(new CommandSetSimulationDistance().build());

        // Adds all command trees to the dispatcher to register the commands.
        dispatcher.register(performantCommands.build());
    }
}
