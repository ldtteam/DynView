package com.dynamic_view.event;

import com.dynamic_view.DynView;
import com.dynamic_view.Utils.TickTimeHandler;
import com.dynamic_view.ViewDistHandler.ServerDynamicViewDistanceManager;
import com.dynamic_view.chunkloadhandler.ChunkLoadTicketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onDedicatedServerTick(final TickEvent.ServerTickEvent event)
    {
        TickTimeHandler.getInstance().onServerTick();
    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void onWorldLoad(final WorldEvent.Load event)
    {
        ServerDynamicViewDistanceManager.getInstance().initViewDist();
    }

    final static boolean chunkslow = DynView.getConfig().getCommonConfig().chunkunload.get();

    @SubscribeEvent
    public static void onChunkLoad(@NotNull final ChunkEvent.Load event)
    {
        if (chunkslow)
        {
            ChunkLoadTicketHandler.getInstance().onChunkLoad(event);
        }
    }

    @SubscribeEvent
    public static void onServerTick(final TickEvent.ServerTickEvent event)
    {
        if (chunkslow)
        {
            ChunkLoadTicketHandler.getInstance().onServerTick(event);
        }
    }
}
