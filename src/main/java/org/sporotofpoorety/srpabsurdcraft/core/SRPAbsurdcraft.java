package org.sporotofpoorety.srpabsurdcraft.core;

import org.sporotofpoorety.srpabsurdcraft.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.dhanantry.scapeandrunparasites.util.SRPAttributes;


import org.sporotofpoorety.srpabsurdcraft.Tags;
import org.sporotofpoorety.srpabsurdcraft.config.SRPAbsurdcraftConfig;
import org.sporotofpoorety.srpabsurdcraft.core.SRPAbsurdcraftCommonProxy;
import org.sporotofpoorety.srpabsurdcraft.config.SRPAbsurdcraftConfig;
import org.sporotofpoorety.srpabsurdcraft.util.SRPOverrideAttributes;
import org.sporotofpoorety.srpabsurdcraft.util.SRPOverrideConfig;
import org.sporotofpoorety.srpabsurdcraft.util.SRPOverrideConfigMobs;
import org.sporotofpoorety.srpabsurdcraft.util.SRPOverrideConfigSystems;
import org.sporotofpoorety.srpabsurdcraft.util.SRPOverrideConfigWorld;








@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,
        guiFactory = "org.sporotofpoorety.srpabsurdcraft.config.SRPAbsurdcraftFactoryGui", dependencies= "required-after:mixinbooter@[10.1,);required-after:ebwizardry;required-after:cleanroom@[0.3.27-alpha,);required-after:potioncore;required-after:srparasites@[1.10.0,1.10.1);required-after:eternitymode;required-after:elenaidodge2;required-after:walljump;required-after:aquaacrobatics;required-after:hungerstrike")
public class SRPAbsurdcraft {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @SidedProxy(
        clientSide = "org.sporotofpoorety.srpabsurdcraft.core.SRPAbsurdcraftClientProxy",
        serverSide = "org.sporotofpoorety.srpabsurdcraft.core.SRPAbsurdcraftCommonProxy"
    )
    public static SRPAbsurdcraftCommonProxy srpAbsurdcraftProxy;


    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
//      LOGGER.info("Hello From {}!", Tags.MOD_NAME);


        srpAbsurdcraftProxy.registerRenderers();
		SRPAbsurdcraftConfig.load(event);

        SRPOverrideAttributes.SRPOverrideAttributesApply();
        SRPOverrideConfig.SRPOverrideConfigApply();
        SRPOverrideConfigMobs.SRPOverrideConfigMobsApply();
        SRPOverrideConfigSystems.SRPOverrideConfigSystemsApply();
        SRPOverrideConfigWorld.SRPOverrideConfigWorldApply();
    }


/*
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        SRPAttributes.reset();
    }
*/

}
