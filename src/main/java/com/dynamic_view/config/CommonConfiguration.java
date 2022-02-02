package com.dynamic_view.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfiguration
{
    public final ForgeConfigSpec.IntValue     minChunkViewDist;
    public final ForgeConfigSpec.IntValue     maxChunkViewDist;
    public final ForgeConfigSpec.IntValue     minSimulationDist;
    public final ForgeConfigSpec.IntValue     maxSimulationDist;
    public final ForgeConfigSpec.BooleanValue adjustSimulationDistance;
    public final ForgeConfigSpec.IntValue     meanAvgTickTime;
    public final ForgeConfigSpec.IntValue     viewDistanceUpdateRate;
    public final ForgeConfigSpec.BooleanValue logMessages;
    public final ForgeConfigSpec.BooleanValue chunkunload;

    protected CommonConfiguration(final ForgeConfigSpec.Builder builder)
    {
        builder.push("Dynamic Chunk View Distance Settings");

        builder.comment("The minimum chunk view distance allowed to use. Default: 4");
        minChunkViewDist = builder.defineInRange("minChunkViewDist", 5, 3, 200);

        builder.comment("The maximum chunk view distance allowed to use. Set to the max a player could benefit from. Default: 12");
        maxChunkViewDist = builder.defineInRange("maxChunkViewDist", 12, 1, 200);

        builder.comment("The minimum simulation distance allowed to use. Default: 4");
        minSimulationDist = builder.defineInRange("minSimulationDist", 4, 1, 200);

        builder.comment("The maximum simulation distance allowed to use. Default: 10");
        maxSimulationDist = builder.defineInRange("maxSimulationDist", 10, 1, 200);

        builder.comment(
          "The average tick time to stabilize the chunk view distance around. Setting it higher than 50ms is not advised, as after 50ms the TPS will go below 20. Default: 45ms");
        meanAvgTickTime = builder.defineInRange("meanAvgTickTime", 45, 10, 100);

        builder.comment("The update frequency of average server tick time checks to update view distances. Default: 30sec");
        viewDistanceUpdateRate = builder.defineInRange("viewDistanceUpdateRate", 30, 1, 1000);

        builder.comment("Whether to adjust the simulation distance aswell, default: true.");
        adjustSimulationDistance = builder.define("adjustSimulationDistance", true);

        builder.comment("Whether to output log messages for actions done. This can be helpful to balance the other settings nicely.");
        logMessages = builder.define("logMessages", true);

        // Escapes the current category level
        builder.pop();

        builder.push("Chunk slow unload settings");

        builder.comment("Enable slow chunk unloading(~1minute) after load, helps with mods hot-loading chunks frequently. Default: true");
        chunkunload = builder.define("chunkunload", true);
    }
}
