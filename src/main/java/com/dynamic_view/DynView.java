package com.dynamic_view;

import com.dynamic_view.event.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import com.dynamic_view.config.Configuration;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(modid = Constants.MOD_ID)
public class DynView
{
    public static final Logger LOGGER;
    @Mod.Instance("dynview")
    public static DynView instance;
    private static Configuration config;

    public DynView() {
        DynView.config = new Configuration();
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        DynView.LOGGER.info("Zooming chunks initiated!");
    }

    public static Configuration getConfig() {
        return DynView.config;
    }

    static {
        LOGGER = LogManager.getLogger();
        MinecraftForge.EVENT_BUS.register((Object)new EventHandler());
    }
}
