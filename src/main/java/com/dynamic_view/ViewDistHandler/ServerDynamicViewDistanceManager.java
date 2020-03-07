package com.dynamic_view.ViewDistHandler;

import com.dynamic_view.DynView;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ServerDynamicViewDistanceManager implements IDynamicViewDistanceManager
{
    private static final int                              UPDATE_LEEWAY        = 2;
    private static       ServerDynamicViewDistanceManager instance;
    private final        int                              minChunkViewDist;
    private final        int                              maxChunkViewDist;
    private final        double                           meanTickToStayBelow;
    private              int                              currentChunkViewDist = 0;

    private ServerDynamicViewDistanceManager()
    {
        minChunkViewDist = DynView.getConfig().getCommonConfig().minChunkViewDist.get();
        maxChunkViewDist = DynView.getConfig().getCommonConfig().maxChunkViewDist.get();
        meanTickToStayBelow = DynView.getConfig().getCommonConfig().meanAvgTickTime.get();
        currentChunkViewDist = minChunkViewDist;
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
        ServerLifecycleHooks.getCurrentServer().getPlayerList().setViewDistance(currentChunkViewDist);
    }

    @Override
    public void updateViewDistForMeanTick()
    {
        final MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

        if (server.getPlayerList().getPlayers().isEmpty())
        {
            return;
        }

        final double meanTickTime = average(server.tickTimeArray) * 1.0E-6D;

        if (meanTickTime - UPDATE_LEEWAY > meanTickToStayBelow && currentChunkViewDist > minChunkViewDist)
        {
            currentChunkViewDist--;
            if (DynView.getConfig().getCommonConfig().logMessages.get())
            {
                DynView.LOGGER.info("Mean tick: " + (Math.round(meanTickTime * 100) / 100) + "ms decreasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerList().setViewDistance(currentChunkViewDist);
        }

        if (meanTickTime + UPDATE_LEEWAY < meanTickToStayBelow && currentChunkViewDist < maxChunkViewDist)
        {
            currentChunkViewDist++;
            if (DynView.getConfig().getCommonConfig().logMessages.get())
            {
                DynView.LOGGER.info("Mean tick: " + (Math.round(meanTickTime * 100) / 100) + "ms increasing chunk view distance to: " + currentChunkViewDist);
            }
            server.getPlayerList().setViewDistance(currentChunkViewDist);
        }
    }

    /**
     * Averages the arrays values.
     *
     * @param values
     * @return
     */
    private static long average(long[] values)
    {
        if (values == null || values.length == 0)
        {
            return 0L;
        }

        long sum = 0L;
        for (long v : values)
        {
            sum += v;
        }
        return sum / values.length;
    }
}
