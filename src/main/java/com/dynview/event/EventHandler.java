package com.dynview.event;

import com.dynview.Utils.TickTimeHandler;
import com.dynview.ViewDistHandler.ServerDynamicViewDistanceManager;
import net.minecraft.server.MinecraftServer;

/**
 * Handler to catch server tick events
 */
public class EventHandler
{
    public static void onDedicatedServerTick(final MinecraftServer server)
    {
        TickTimeHandler.getInstance().onServerTick(server);
    }

    public static void onServerStarted(final MinecraftServer server)
    {
        ServerDynamicViewDistanceManager.getInstance().initViewDist(server);
    }
}
