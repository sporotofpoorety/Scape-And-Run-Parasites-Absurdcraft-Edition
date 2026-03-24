package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import java.util.List;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;


import org.sporotofpoorety.srpabsurdcraft.util.ParasiteSpawnUtils;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;




//Mixin this class
@Mixin(value = ParasiteEventEntity.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinParasiteEventEntity
{

/*
    @Inject
    (
        method = "convertEntity",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void conversionRespectsCap(EntityLivingBase entityin, NBTTagCompound tags, boolean ignoreKey, String[] list, CallbackInfoReturnable<Boolean> callInfo)
    {
//This might be laggy,
//especially with dispatchers, but 
//unfortunately SRP itself doesn't store current parasite count (ugh)
        if (entityin != null) 
        {
//Get world
            World world = entityin.world;

//Server side
            if (!world.isRemote) 
            {
//Get loaded entities
                List<Entity> serverList = world.loadedEntityList;
//For parasite count
                int parasiteCount = 0;


//For each loaded entity
                for(int x = 0; x < serverList.size(); ++x) 
                {
//Get entity
                    Entity loadedEntity = serverList.get(x);

//If standard parasite
                    if(ParasiteSpawnUtils.isStandardParasite(loadedEntity)) 
                    {
                        ++parasiteCount;
                    }
                }


//If count below max (including players)
                if(parasiteCount < SRPConfig.worldMobCap + (parasiteCount * SRPConfig.worldMobCapPlusPlayer))
                {
//Cancel conversion    
                    callInfo.setReturnValue(false);
                    callInfo.cancel();
                }
            }
        }
    }
*/

}
