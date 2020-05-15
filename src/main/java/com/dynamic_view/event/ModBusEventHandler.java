package com.dynamic_view.event;

import com.dynamic_view.DynView;
import com.dynamic_view.Utils.TickTimeHandler;
import com.dynamic_view.ViewDistHandler.ServerDynamicViewDistanceManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import static com.dynamic_view.Constants.TICKS_A_SECOND;

public class ModBusEventHandler
{
    @SubscribeEvent
    public static void onConfigChanged(ModConfig.ModConfigEvent event)
    {
        TickTimeHandler.serverTickTimerInterval = DynView.getConfig().getCommonConfig().viewDistanceUpdateRate.get() * TICKS_A_SECOND;

        // Server
        ServerDynamicViewDistanceManager.minChunkViewDist = DynView.getConfig().getCommonConfig().minChunkViewDist.get();
        ServerDynamicViewDistanceManager.maxChunkViewDist = DynView.getConfig().getCommonConfig().maxChunkViewDist.get();
        ServerDynamicViewDistanceManager.meanTickToStayBelow = DynView.getConfig().getCommonConfig().meanAvgTickTime.get();

        // Client
    }
}
