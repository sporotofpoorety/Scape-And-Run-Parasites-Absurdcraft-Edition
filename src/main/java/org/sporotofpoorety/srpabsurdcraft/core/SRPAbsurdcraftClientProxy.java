package org.sporotofpoorety.srpabsurdcraft.core;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;


import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;


import com.dhanantry.scapeandrunparasites.client.renderer.entity.misc.RenderOrbVoid;


import org.sporotofpoorety.srpabsurdcraft.client.render.RenderOrbVoidCustom;
import org.sporotofpoorety.srpabsurdcraft.core.SRPAbsurdcraftCommonProxy;
import org.sporotofpoorety.srpabsurdcraft.entity.*;




public class SRPAbsurdcraftClientProxy extends SRPAbsurdcraftCommonProxy {

    @Override
    public void registerRenderers() 
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityOrbVoidCustom.class, new IRenderFactory<EntityOrbVoidCustom>() 
        {
            public Render<? super EntityOrbVoidCustom> createRenderFor(RenderManager manager) 
            {
               return new RenderOrbVoid(manager);
            }
        });
    }
}
