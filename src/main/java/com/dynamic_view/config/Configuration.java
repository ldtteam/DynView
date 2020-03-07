package com.dynamic_view.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class Configuration
{
    /**
     * Loaded everywhere, not synced
     */
    private final CommonConfiguration commonConfig;

    /**
     * Loaded clientside, not synced
     */
    // private final ClientConfiguration clientConfig;

    /**
     * Builds configuration tree.
     */
    public Configuration()
    {
        final Pair<CommonConfiguration, ForgeConfigSpec> com = new ForgeConfigSpec.Builder().configure(CommonConfiguration::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, com.getRight());

        commonConfig = com.getLeft();
    }

    public CommonConfiguration getCommonConfig()
    {
        return commonConfig;
    }
}
