package com.dynamic_view.event;

import com.dynamic_view.DynView;
import com.dynamic_view.ViewDistHandler.ServerDynamicViewDistanceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.dynamic_view.Constants.TICKS_A_SECOND;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    /**
     * Tick timer for update frequency
     */
    private static int serverTickTimer = 0;

    /**
     * Update frequency
     */
    private static final int serverTickTimerInterval = DynView.getConfig().getCommonConfig().viewDistanceUpdateRate.get() * TICKS_A_SECOND;

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onDedicatedServerTick(final TickEvent.ServerTickEvent event)
    {
        serverTickTimer++;
        if (serverTickTimer == serverTickTimerInterval)
        {
            ServerDynamicViewDistanceManager.getInstance().updateViewDistForMeanTick();
            serverTickTimer = 0;
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onWorldLoad(final WorldEvent.Load event)
    {
        ServerDynamicViewDistanceManager.getInstance().initViewDist();
    }
}
