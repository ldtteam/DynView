package com.dynamic_view.ViewDistHandler;

import com.dynamic_view.DynView;
import net.minecraft.server.MinecraftServer;
import com.dynamic_view.config.Configuration;
import net.minecraftforge.fml.server.FMLServerHandler;

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
        FMLServerHandler.instance().getServer().getPlayerList().setViewDistance(this.currentChunkViewDist);
    }

    @Override
    public void updateViewDistForMeanTick(final int meanTickTime)
    {
        final MinecraftServer server = FMLServerHandler.instance().getServer();

        if (server.getPlayerList().getPlayers().isEmpty())
        {
            return;
        }

        if (meanTickTime - UPDATE_LEEWAY > meanTickToStayBelow && currentChunkViewDist > minChunkViewDist)
        {
            currentChunkViewDist--;
            if (Configuration.dynamicChunk.logMessages)
            {
                DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerList().setViewDistance(currentChunkViewDist);
        }

        if (meanTickTime + UPDATE_LEEWAY < meanTickToStayBelow && currentChunkViewDist < maxChunkViewDist)
        {
            currentChunkViewDist++;
            if (Configuration.dynamicChunk.logMessages)
            {
                DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerList().setViewDistance(currentChunkViewDist);
        }
    }
}
