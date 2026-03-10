package org.sporotofpoorety.srpabsurdcraft.config;

import net.minecraftforge.common.config.Configuration;

public class SRPAbsurdcraftConfigGeneral
{

    public static boolean placeholderVal;
    public static int softHardcoreLength;

	public static void load(Configuration config) 
    {
//Adds config category
		String category1 = "PLACEHOLDER CATEGORY";
		config.addCustomCategoryComment(category1, "This category is a placeholder");

//Format is category, key, default value, comment
        placeholderVal = config.get(category1, "Placeholder value", false, "It's a placeholder'.").getBoolean();
	}


    public static boolean getPlaceholderVal()
    {
        return placeholderVal;
    }

}
