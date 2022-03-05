package com.dynview.ViewDistHandler;

import com.dynview.DynView;
import net.minecraft.server.MinecraftServer;

public class ServerDynamicViewDistanceManager implements IDynamicViewDistanceManager
{
    private static final int                              UPDATE_LEEWAY = 3;
    private static       ServerDynamicViewDistanceManager instance;
    public static        int                              minChunkViewDist;
    public static        int                              maxChunkViewDist;
    public static        int                              minChunkUpdateDist;
    public static        int                              maxChunkUpdateDist;
    public static        double                           meanTickToStayBelow;

    private boolean reduceViewDistance   = false;
    private boolean increaseViewDistance = true;

    private int             currentChunkViewDist   = 0;
    private int             currentChunkUpdateDist = 0;
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
        currentChunkViewDist = (minChunkViewDist + maxChunkViewDist) / 2;
        currentChunkUpdateDist = (minChunkUpdateDist + maxChunkUpdateDist) / 2;
        server.getPlayerManager().setViewDistance(minChunkViewDist);
        server.getWorlds().forEach(level -> level.getChunkManager().applySimulationDistance(currentChunkUpdateDist));
        this.server = server;
    }

    @Override
    public void updateViewDistForMeanTick(final int meanTickTime)
    {
        if (server.getPlayerManager().getPlayerList().isEmpty())
        {
            return;
        }

        if (meanTickTime - UPDATE_LEEWAY > meanTickToStayBelow)
        {
            increaseViewDistance = true;

            if (reduceViewDistance && currentChunkViewDist > minChunkViewDist)
            {
                reduceViewDistance = false;
                currentChunkViewDist--;
                if (DynView.getConfig().getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerManager().setViewDistance(currentChunkViewDist);
                return;
            }

            if (!reduceViewDistance && currentChunkUpdateDist > minChunkUpdateDist)
            {
                reduceViewDistance = true;
                currentChunkUpdateDist--;
                if (DynView.getConfig().getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getWorlds().forEach(level -> level.getChunkManager().applySimulationDistance(currentChunkUpdateDist));
            }

            if (!DynView.getConfig().getCommonConfig().adjustSimulationDistance)
            {
                reduceViewDistance = true;
            }
        }

        if (meanTickTime + UPDATE_LEEWAY < meanTickToStayBelow)
        {
            reduceViewDistance = false;

            if (increaseViewDistance && currentChunkViewDist < maxChunkViewDist)
            {
                increaseViewDistance = false;
                currentChunkViewDist++;
                if (DynView.getConfig().getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerManager().setViewDistance(currentChunkViewDist);
                return;
            }

            if (!increaseViewDistance && currentChunkUpdateDist < maxChunkUpdateDist)
            {
                increaseViewDistance = true;
                currentChunkUpdateDist++;
                if (DynView.getConfig().getCommonConfig().logMessages)
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getWorlds().forEach(level -> level.getChunkManager().applySimulationDistance(currentChunkUpdateDist));
            }

            if (!DynView.getConfig().getCommonConfig().adjustSimulationDistance)
            {
                increaseViewDistance = true;
            }
        }
    }
}
