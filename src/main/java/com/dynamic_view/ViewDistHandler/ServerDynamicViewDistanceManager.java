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
    public static        int                              minChunkUpdateDist;
    public static        int                              maxChunkUpdateDist;
    public static        double                           meanTickToStayBelow;

    private boolean reduceViewDistance   = false;
    private boolean increaseViewDistance = true;

    private int currentChunkViewDist   = 0;
    private int currentChunkUpdateDist = 0;

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
        currentChunkViewDist = (minChunkViewDist + maxChunkViewDist) / 2;
        currentChunkUpdateDist = (minChunkUpdateDist + maxChunkUpdateDist) / 2;
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

        if (meanTickTime - UPDATE_LEEWAY > meanTickToStayBelow)
        {
            increaseViewDistance = true;

            if (reduceViewDistance && currentChunkViewDist > minChunkViewDist)
            {
                reduceViewDistance = false;
                currentChunkViewDist--;
                if (DynView.getConfig().getCommonConfig().logMessages.get())
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerList().setViewDistance(currentChunkViewDist);
                return;
            }

            if (!reduceViewDistance && currentChunkUpdateDist > minChunkUpdateDist)
            {
                reduceViewDistance = true;
                currentChunkUpdateDist--;
                if (DynView.getConfig().getCommonConfig().logMessages.get())
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms decreasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
            }

            if (!DynView.getConfig().getCommonConfig().adjustSimulationDistance.get())
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
                if (DynView.getConfig().getCommonConfig().logMessages.get())
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing chunk view distance to: " + currentChunkViewDist);
                }
                server.getPlayerList().setViewDistance(currentChunkViewDist);
                return;
            }

            if (!increaseViewDistance && currentChunkUpdateDist < maxChunkUpdateDist)
            {
                increaseViewDistance = true;
                currentChunkUpdateDist++;
                if (DynView.getConfig().getCommonConfig().logMessages.get())
                {
                    DynView.LOGGER.info("Mean tick: " + meanTickTime + "ms increasing simulation distance to: " + currentChunkUpdateDist);
                }
                server.getAllLevels().forEach(level -> level.getChunkSource().setSimulationDistance(currentChunkUpdateDist));
            }

            if (!DynView.getConfig().getCommonConfig().adjustSimulationDistance.get())
            {
                increaseViewDistance = true;
            }
        }
    }
}
