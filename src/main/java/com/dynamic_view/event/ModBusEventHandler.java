package com.dynamic_view.event;

import com.dynamic_view.DynView;
import com.dynamic_view.Utils.TickTimeHandler;
import com.dynamic_view.ViewDistHandler.ServerDynamicViewDistanceManager;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Comparator;

import static com.dynamic_view.Constants.TICKS_A_SECOND;

public class ModBusEventHandler
{
    private static final TicketType<ChunkPos> LUNKOWN = TicketType.create("unknown", Comparator.comparingLong(ChunkPos::toLong), 1201);
    private static final TicketType<ChunkPos> OUNKOWN = TicketType.UNKNOWN;

    @SubscribeEvent
    public static void onConfigChanged(ModConfigEvent event)
    {
        TickTimeHandler.serverTickTimerInterval = DynView.getConfig().getCommonConfig().viewDistanceUpdateRate.get() * TICKS_A_SECOND;

        // Server
        ServerDynamicViewDistanceManager.minChunkViewDist = DynView.getConfig().getCommonConfig().minChunkViewDist.get();
        ServerDynamicViewDistanceManager.maxChunkViewDist = DynView.getConfig().getCommonConfig().maxChunkViewDist.get();
        ServerDynamicViewDistanceManager.meanTickToStayBelow = DynView.getConfig().getCommonConfig().meanAvgTickTime.get();

        if (DynView.getConfig().getCommonConfig().chunkunload.get())
        {
            TicketType.UNKNOWN = LUNKOWN;
        }
        else
        {
            TicketType.UNKNOWN = OUNKOWN;
        }
        // Client
    }
}
