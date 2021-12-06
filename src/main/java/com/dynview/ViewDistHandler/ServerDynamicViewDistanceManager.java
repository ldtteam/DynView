package com.dynview.ViewDistHandler;

import com.dynview.DynView;
import net.minecraft.server.MinecraftServer;

public class ServerDynamicViewDistanceManager implements IDynamicViewDistanceManager
{
    private static final int                              UPDATE_LEEWAY = 3;
    private static       ServerDynamicViewDistanceManager instance;
    public static        int                              minChunkViewDist;
    public static        int                              maxChunkViewDist;
    public static        double                           meanTickToStayBelow;

    private int             currentChunkViewDist = 0;
    private MinecraftServer server;

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
    public void initViewDist(final MinecraftServer server)
    {
        currentChunkViewDist = minChunkViewDist;
        server.getPlayerManager().setViewDistance(minChunkViewDist);
        this.server = server;
    }

    @Override
    public void updateViewDistForMeanTick(final int meanTickTime)
    {
        if (server.getPlayerManager().getPlayerList().isEmpty())
        {
            return;
        }

        if (meanTickTime - UPDATE_LEEWAY > meanTickToStayBelow && currentChunkViewDist > minChunkViewDist)
        {
            currentChunkViewDist--;
            if (DynView.getConfig().getCommonConfig().logMessages)
            {
                DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerManager().setViewDistance(currentChunkViewDist);
        }

        if (meanTickTime + UPDATE_LEEWAY < meanTickToStayBelow && currentChunkViewDist < maxChunkViewDist)
        {
            currentChunkViewDist++;
            if (DynView.getConfig().getCommonConfig().logMessages)
            {
                DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerManager().setViewDistance(currentChunkViewDist);
        }
    }
}
