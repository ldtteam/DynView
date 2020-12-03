package com.dynamic_view.event;

import com.dynamic_view.Utils.TickTimeHandler;
import com.dynamic_view.config.Configuration;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.dynamic_view.ViewDistHandler.ServerDynamicViewDistanceManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    @SubscribeEvent
    public static void onDedicatedServerTick(final TickEvent.ServerTickEvent event)
    {
        TickTimeHandler.getInstance().onServerTick();
    }

    @SubscribeEvent
    public static void onWorldLoad(final WorldEvent.Load event)
    {
        ServerDynamicViewDistanceManager.getInstance().initViewDist();
    }
}
