package org.sporotofpoorety.srpabsurdcraft.config;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import org.sporotofpoorety.srpabsurdcraft.Tags;

public class SRPAbsurdcraftGuiConfig extends GuiConfig {

	public SRPAbsurdcraftGuiConfig(GuiScreen parentScreen) 
	{
		super(parentScreen, getConfigElements(), Tags.MOD_ID, false, false, "srpabsurdcraft.config.title");
	}

	private static List<IConfigElement> getConfigElements() 
	{
		return SRPAbsurdcraftConfig.config.getCategoryNames().stream()
				.map(categoryName -> new ConfigElement(SRPAbsurdcraftConfig.config.getCategory(categoryName).setLanguageKey("srpabsurdcraft.config." + categoryName)))
				.collect(Collectors.toList());
	}
}
