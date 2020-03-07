package com.dynamic_view.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfiguration
{
    public final ForgeConfigSpec.IntValue minChunkViewDist;
    public final ForgeConfigSpec.IntValue maxChunkViewDist;
    public final ForgeConfigSpec.IntValue meanAvgTickTime;
    public final ForgeConfigSpec.IntValue viewDistanceUpdateRate;

    protected CommonConfiguration(final ForgeConfigSpec.Builder builder)
    {
        builder.push("Dynamic chunk view distance settings");

        builder.comment("The minimum chunk view distance allowed to use, set to what players should get at least. default: 4");
        minChunkViewDist = builder.defineInRange("minChunkViewDist", 4, 1, 200);

        builder.comment("The maximum chunk view distance allowed to use, set to the max which a player could benefit from. default: 20");
        maxChunkViewDist = builder.defineInRange("maxChunkViewDist", 20, 1, 200);

        builder.comment(
          "The average tick time to stabilize the chunk view distance around, setting it higher than 50ms is not advised, as after 50ms the tps will go below 20. default:40ms");
        meanAvgTickTime = builder.defineInRange("meanAvgTickTime", 45, 10, 100);

        builder.comment("The update frequency of average server tick time checks to update view distances, default is every 30 seconds");
        viewDistanceUpdateRate = builder.defineInRange("viewDistanceUpdateRate", 30, 1, 1000);

        // Escapes the current category level
        builder.pop();
    }
}
