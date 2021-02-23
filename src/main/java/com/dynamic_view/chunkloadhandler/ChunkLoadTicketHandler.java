package com.dynamic_view.chunkloadhandler;

import com.dynamic_view.Constants;
import com.dynamic_view.DynView;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChunkLoadTicketHandler implements IChunkLoadTicketHandler
{
    private static ChunkLoadTicketHandler instance;

    public static ChunkLoadTicketHandler getInstance()
    {
        if (instance == null)
        {
            instance = new ChunkLoadTicketHandler();
        }
        return instance;
    }

    public static final TicketType<ChunkPos>                     TEMP_LOAD       =
      TicketType.create(Constants.MOD_ID + ":" + "initial_load", Comparator.comparingLong(ChunkPos::asLong));
    private             Map<RegistryKey<World>, WorldTicketList> worldToChunkMap = new HashMap();

    public void onChunkLoad(@NotNull final ChunkEvent.Load event)
    {
        if (!(event.getWorld() instanceof ServerWorld))
        {
            return;
        }

        final ServerWorld world = (ServerWorld) event.getWorld();
        WorldTicketList worldTickets = worldToChunkMap.get(world.getDimensionKey());
        if (worldTickets == null)
        {
            worldTickets = new WorldTicketList();
        }
        worldTickets.ticketList.add(new WorldTicketList.TicketPosAndTime(
          event.getChunk().getPos().asLong(), System.currentTimeMillis() + event.getWorld().getRandom().nextInt(60) * 1000 + 60000));
        DynView.LOGGER.warn("Registering chunk at:" + event.getChunk().getPos());
        world.getChunkProvider().registerTicket(TEMP_LOAD, event.getChunk().getPos(), 1, event.getChunk().getPos());
    }

    public void onServerTick(final TickEvent.ServerTickEvent event)
    {
        final long currentTime = System.currentTimeMillis();
        for (final Map.Entry<RegistryKey<World>, WorldTicketList> entry : worldToChunkMap.entrySet())
        {
            final WorldTicketList worldTicketList = entry.getValue();
            final ServerWorld world = ServerLifecycleHooks.getCurrentServer().getWorld(entry.getKey());
            final WorldTicketList.TicketPosAndTime posAndTime = worldTicketList.ticketList.get(worldTicketList.getAndIterateIndex());

            if (posAndTime.time < currentTime)
            {
                // Check this is the right actual pos in world
                final ChunkPos chunkPos = new ChunkPos(posAndTime.pos);
                world.getChunkProvider().releaseTicket(TEMP_LOAD, chunkPos, 1, chunkPos);
                DynView.LOGGER.warn("Un-Registering chunk at:" + chunkPos);
            }
        }
    }

    /**
     * Class holding the ticket list and the current index
     */
    private static class WorldTicketList
    {
        private int                    index      = 0;
        private List<TicketPosAndTime> ticketList = new ArrayList();

        private int getAndIterateIndex()
        {
            index++;
            if (index == ticketList.size())
            {
                index = 0;
            }

            return index;
        }

        private static class TicketPosAndTime
        {
            final long pos;
            final long time;

            private TicketPosAndTime(final long pos, final long time)
            {
                this.pos = pos;
                this.time = time;
            }
        }
    }
}
