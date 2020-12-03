package com.dynamic_view.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.config.Config;

@Config(modid = "dynview")
public class Configuration
{
    @Config.Comment({ "Dynamic chunk view distance settings" })
    public static DynamicChunk dynamicChunk;

    static {
        Configuration.dynamicChunk = new DynamicChunk();
    }

    public static class DynamicChunk
    {
        @Config.Comment({ "The minimum chunk view distance allowed to use, set to what players should get at least. default: 4" })
        @Config.RangeInt(min = 1, max = 200)
        public int minChunkViewDist;
        @Config.Comment({ "The maximum chunk view distance allowed to use, set to the max which a player could benefit from. default: 20" })
        @Config.RangeInt(min = 1, max = 200)
        public int maxChunkViewDist;
        @Config.Comment({ "The average tick time to stabilize the chunk view distance around, setting it higher than 50ms is not advised, as after 50ms the tps will go below 20. default:40ms" })
        @Config.RangeInt(min = 10, max = 100)
        public int meanAvgTickTime;
        @Config.Comment({ "The update frequency of average server tick time checks to update view distances, default is every 30 seconds" })
        @Config.RangeInt(min = 1, max = 1000)
        public int viewDistanceUpdateRate;
        @Config.Comment({ "Whether to output log messages for actions done, its helpful to balance the other settings nicely." })
        public boolean logMessages;

        public DynamicChunk() {
            this.minChunkViewDist = 4;
            this.maxChunkViewDist = 20;
            this.meanAvgTickTime = 45;
            this.viewDistanceUpdateRate = 30;
            this.logMessages = true;
        }
    }
}
