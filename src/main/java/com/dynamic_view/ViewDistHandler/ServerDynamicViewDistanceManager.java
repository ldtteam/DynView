package com.dynamic_view.ViewDistHandler;

import com.dynamic_view.DynView;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ServerDynamicViewDistanceManager implements IDynamicViewDistanceManager
{
    private static final int                              UPDATE_LEEWAY = 3;
    private static       ServerDynamicViewDistanceManager instance;
    public static        int                              minChunkViewDist;
    public static        int                              maxChunkViewDist;
    public static        double                           meanTickToStayBelow;

    private int currentChunkViewDist = 0;

    private ServerDynamicViewDistanceManager()
    {
    }

    public static IDynamicViewDistanceManager getInstance()
    {
        if (instance == null)
        {
            instance = new ServerDynamicViewDistanceManager();
        }
        return instance;
    }

    @Override
    public void initViewDist()
    {
        currentChunkViewDist = minChunkViewDist;
        ServerLifecycleHooks.getCurrentServer().getPlayerList().setViewDistance(minChunkViewDist);
    }

    @Override
    public void updateViewDistForMeanTick(final int meanTickTime)
    {
        final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server.getPlayerList().getPlayers().isEmpty())
        {
            return;
        }

        if (meanTickTime - UPDATE_LEEWAY > meanTickToStayBelow && currentChunkViewDist > minChunkViewDist)
        {
            currentChunkViewDist--;
            if (DynView.getConfig().getCommonConfig().logMessages.get())
            {
                DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerList().setViewDistance(currentChunkViewDist);
        }

        if (meanTickTime + UPDATE_LEEWAY < meanTickToStayBelow && currentChunkViewDist < maxChunkViewDist)
        {
            currentChunkViewDist++;
            if (DynView.getConfig().getCommonConfig().logMessages.get())
            {
                DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerList().setViewDistance(currentChunkViewDist);
        }
    }
}
