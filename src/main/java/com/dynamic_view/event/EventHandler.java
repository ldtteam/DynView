package com.dynamic_view.event;

import com.dynamic_view.Utils.TickTimeHandler;
import com.dynamic_view.ViewDistHandler.ServerDynamicViewDistanceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onDedicatedServerTick(final TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            return;
        }
        TickTimeHandler.getInstance().onServerTick();
    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onWorldLoad(final WorldEvent.Load event)
    {
        ServerDynamicViewDistanceManager.getInstance().initViewDist();
    }
}
