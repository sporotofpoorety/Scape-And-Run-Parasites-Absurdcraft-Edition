package org.sporotofpoorety.srpabsurdcraft.config;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.sporotofpoorety.srpabsurdcraft.Tags;



public class SRPAbsurdcraftConfig 
{

	static Configuration config;


	public static void load(FMLPreInitializationEvent event) 
    {
		File dir = getSRPAbsurdcraftConfigurationLocation(event);
		if(!dir.exists())
		    { dir.mkdirs(); }

		config = new Configuration(new File(dir, "srpabsurdcraft.cfg"));
		reloadConfig();
		

		MinecraftForge.EVENT_BUS.register(new SRPAbsurdcraftConfig());
	}


	private static void reloadConfig() 
    {		
		SRPAbsurdcraftConfigGeneral.load(config);
		SRPAbsurdcraftConfigMobs.load(config);

		if (config.hasChanged()) 
        {
			config.save();
		}
	}


	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) 
    {
		if (event.getModID().equals(Tags.MOD_ID)) 
        {
			reloadConfig();
		}
	}
	

	public static File getSRPAbsurdcraftConfigurationLocation(FMLPreInitializationEvent event)
	{
		return new File(event.getModConfigurationDirectory(), "srpabsurdcraft");
	}
}
