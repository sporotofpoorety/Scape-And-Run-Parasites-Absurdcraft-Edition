package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import javax.annotation.Nullable;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;


import com.dhanantry.scapeandrunparasites.entity.ai.EntityAISkill;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityCanHaveBodies;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;
import com.dhanantry.scapeandrunparasites.util.handlers.SRPEventHandlerBus;
import com.dhanantry.scapeandrunparasites.world.SRPSaveData;
import com.dhanantry.scapeandrunparasites.world.SRPWorldData;


import org.sporotofpoorety.eternitymode.entity.EntityExplosiveShockwave;




//Mixin this class
@Mixin(value = SRPEventHandlerBus.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinSRPEventHandlerBus
{


    @Inject
    (
        method = "setNewParasiteTask",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
//Stripped down
    private void overrideStatIncrease
    (EntityParasiteBase entity, String mobname, boolean flagNC, SRPWorldData data, CallbackInfo callInfo) 
    {
        SRPEventHandlerBus self = (SRPEventHandlerBus) (Object) this;
        SRPSaveData dataS = SRPSaveData.get(entity.world, 82);
        int len;

        if (!entity.spawnedByColo) 
        {
            if (entity instanceof EntityCanHaveBodies) 
            {
                EntityCanHaveBodies head = (EntityCanHaveBodies)entity;
                if (head.getCanF()) 
                {
                    len = head.getBodyLength();
                    EntityCanHaveBodies current = head;

                    for(int i = 0; i < len; ++i) 
                    {
                        EntityCanHaveBodies entityWithBodies = head.getAnotherBody(entity.world);
                        entityWithBodies.setCanF(false);
                        entityWithBodies.setFollowing(current);
                        entityWithBodies.copyCopy(current);
                        entityWithBodies.onSpawn(entity.world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
                        entity.world.spawnEntity(entityWithBodies.getEntity());
                        entityWithBodies.setBodyNumber(i + 1);
                        if (len - 1 == i) 
                        {
                            entityWithBodies.setBodyTail(true);
                        }

                        current = entityWithBodies;
                    }
                }
            }
        }

        if (SRPConfig.parasiteGriefing.length != 0) 
        {
            String[] task = new String[4];

            for(len = 0; len < SRPConfig.parasiteGriefing.length; ++len) 
            {
                if (SRPConfig.parasiteGriefing[len] != null) 
                {
                    task = SRPConfig.parasiteGriefing[len].split(";");
                    if (task[0].equals(mobname)) 
                    {
                        if (entity instanceof EntityPStationary) 
                        {
                            entity.setSkillBreakBlocksValues(Float.parseFloat(task[1]), MathHelper.ceil(entity.height), Integer.parseInt(task[3]));
                        } 
                        else 
                        {
                            entity.setSkillBreakBlocksValues(Float.parseFloat(task[1]), MathHelper.ceil(entity.height), Integer.parseInt(task[3]));
                            entity.tasks.addTask(9, new EntityAISkill(entity, Integer.parseInt(task[2]), 64, false, 13));
                        }
                        break;
                    }
                }
            }
        }




        callInfo.cancel();
    }
}
