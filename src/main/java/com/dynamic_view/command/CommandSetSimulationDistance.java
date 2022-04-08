package com.dynamic_view.command;

import com.dynamic_view.ViewDistHandler.ServerDynamicViewDistanceManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;

public class CommandSetSimulationDistance implements IMCOPCommand
{

    private static final String RANGE_ARG = "chunkdistance";

    @Override
    public int onExecute(final CommandContext<CommandSourceStack> context)
    {
        context.getSource().sendFailure(new TextComponent("You have to enter a value for the chunk view distance [0-32]"));
        return 0;
    }

    private int executeWithPage(final CommandContext<CommandSourceStack> context)
    {
        if (!checkPreCondition(context))
        {
            return 0;
        }

        ServerDynamicViewDistanceManager.getInstance().setCurrentChunkUpdateDist(IntegerArgumentType.getInteger(context, RANGE_ARG));
        context.getSource().sendSuccess(new TextComponent("Set simulation distance to:" + IntegerArgumentType.getInteger(context, RANGE_ARG)), true);
        return 1;
    }

    @Override
    public String getName()
    {
        return "setsimulationdistance";
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build()
    {
        return IMCCommand.newLiteral(getName())
          .then(IMCCommand.newArgument(RANGE_ARG, IntegerArgumentType.integer(1)).executes(this::executeWithPage)).executes(this::checkPreConditionAndExecute);
    }
}
