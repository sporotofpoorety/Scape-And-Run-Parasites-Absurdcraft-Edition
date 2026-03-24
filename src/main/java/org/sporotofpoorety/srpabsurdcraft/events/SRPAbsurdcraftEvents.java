package org.sporotofpoorety.srpabsurdcraft.events;


import java.util.List;
import java.util.Random;
import java.util.UUID;


import com.google.common.base.Predicate;
import javax.annotation.Nullable;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;


import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


import com.dhanantry.scapeandrunparasites.entity.ai.EntityAINearestAttackableTargetStatus;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPAssimara;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPFeral;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPInfected;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityKol;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.EntityLodo;
import com.dhanantry.scapeandrunparasites.init.SRPPotions;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;


import org.sporotofpoorety.srpabsurdcraft.interfacemixins.IMixinEntityParasiteBase;
import org.sporotofpoorety.srpabsurdcraft.util.ParasiteSpawnUtils;




public class SRPAbsurdcraftEvents 
{

//Event listeners RNG
    private final Random rand = new Random();


//Manipulate parasites on join
    @SubscribeEvent
    public void parasiteJoinManipulate(EntityJoinWorldEvent event)
    { 
        World world = event.getWorld();


        if(!world.isRemote)
        {
//Get entity
            Entity entity = event.getEntity();


//If entity is parasite
            if(entity instanceof EntityParasiteBase)
            {
//Get parasite
                EntityParasiteBase parasite = (EntityParasiteBase) entity;
//Get parasite mob name
                String parasiteName = EntityList.getKey(parasite).getPath();


//If parasite assimara, feral or infected from conversion
                if(ParasiteSpawnUtils.isCothVictim(parasiteName))
                {           
//Get loaded entities
                    List<Entity> loadedEntities = world.loadedEntityList;
//Count converted
                    int convertedCount = 0;

//For each one
                    for(Entity loadedEntity : loadedEntities)
//If is a converted
                    {
                        if(loadedEntity instanceof EntityParasiteBase)
                        {
                            EntityParasiteBase loadedParasite = (EntityParasiteBase) loadedEntity;   
                            String loadedParasiteName = EntityList.getKey(loadedParasite).getPath();

                            if(ParasiteSpawnUtils.isCothVictim(loadedParasiteName))
                            {
//Increase COTH victim count
                                ++convertedCount;
                            }
                        }
                    }


//If too many COTH victims
                    if(convertedCount > 20)
                    {
                        IMixinEntityParasiteBase parasiteMixin = (IMixinEntityParasiteBase) parasite;
//Explode entity
                        parasiteMixin.selfExplodePublic();
/*
                        entity.setDead();
                        event.setCanceled(true);
                        return;
*/
                    }
                }


//Get id
                int parasiteId = parasite.getParasiteIDRegister();

//If not a buglin or worker
                if(parasiteId != 5 && parasiteId != 36)
                {
//Give task to target living entities filtered by predicate
                    parasite.targetTasks.addTask(4, new EntityAINearestAttackableTargetStatus(parasite, EntityLiving.class, 0, false, false, 
                    new Predicate<EntityLiving>() 
                    {
                        public boolean apply(@Nullable EntityLiving candidate) 
                        {
//Return true if animal or villager, and no COTH
                            return (candidate instanceof EntityAnimal || candidate instanceof EntityVillager) && !(candidate.isPotionActive(SRPPotions.COTH_E));
                        }
                    }, SRPConfig.pureSneakPen, SRPConfig.pureInviPen));
                }
            }
        }
    }

}
