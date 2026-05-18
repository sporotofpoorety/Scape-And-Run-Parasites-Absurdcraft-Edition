package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import com.google.common.base.Predicate;


import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;


import javax.annotation.Nullable;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;


import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityHostII;
import com.dhanantry.scapeandrunparasites.util.ParasiteEventEntity;


import org.sporotofpoorety.eternitymode.interfacemixins.IMixinEntityLiving;
import org.sporotofpoorety.eternitymode.util.BlockUtil;
import org.sporotofpoorety.eternitymode.util.QueuedActionAtPos;




//Mixin this class
@Mixin(value = EntityHostII.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinEntityHostII
{

    @Inject
    (
        method = "teleportByGround",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
//Schedule teleport
    private void cancelTeleportByGround(CallbackInfo callInfo)
    {
        callInfo.cancel();
    }




    @Inject
    (
//onLivingUpdate
        method = "func_70636_d",
        at = @At("TAIL"),
        require = 1
    )
//Check teleport conditions
    private void scheduleTeleportByGround(CallbackInfo callInfo) 
    {
        Entity selfEntity = (Entity) (Object) this; 
        EntityLiving selfLiving = (EntityLiving) (Object) this; 


        if(!selfEntity.world.isRemote)
        {
//Every 10 seconds
            if (selfEntity.ticksExisted % 200 == 0) 
            {
//Get target
                EntityLivingBase target = selfLiving.getAttackTarget();
//If target not null and teleport conditions valid
                if (target != null && this.teleportConditions(target.posX, target.posY + 0.5D, target.posZ))
                {
//Make scheduled teleport
                    QueuedActionAtPos scheduledTeleport = new QueuedActionAtPos(target.posX, target.posY + 0.5D, target.posZ, selfEntity.world.getTotalWorldTime() + 90, 0); 
//Add scheduled teleport to this
                    ((IMixinEntityLiving) selfLiving).addQueuedAction(scheduledTeleport);
                }  
            }
        }
    }




//Telegraph teleport
    public void queuedActionBefore(QueuedActionAtPos queuedAction)
    {
        Entity selfEntity = (Entity) (Object) this; 


        if((selfEntity.ticksExisted % 5) == 0)
        {
//If still has valid teleport conditions
            if(this.teleportConditions(queuedAction.actionX, queuedAction.actionY, queuedAction.actionZ))
            {
//Telegraph particles
                for(int height = -1; height <= 3; height++)
                {
                    selfEntity.world.spawnParticle
                        (EnumParticleTypes.EXPLOSION_LARGE, queuedAction.actionX, queuedAction.actionY + (double) height, queuedAction.actionZ, 1.0D, 0.0D, 0.0D);
                }
            }
        }
    }


//Execute teleport
    public void queuedActionExecute(QueuedActionAtPos queuedAction)
    {
        Entity selfEntity = (Entity) (Object) this; 
        EntityLivingBase selfLivingBase = (EntityLivingBase) (Object) this; 


//Get suitable blockpos
        BlockPos telePos = BlockUtil.findFirstSolidBlock
            (selfEntity.world, (int) queuedAction.actionX, (int) queuedAction.actionY, (int) queuedAction.actionZ, 
            1.0F, 10, 1);

//If blockpos not null, go to it
        if(telePos != null)
        {
            selfLivingBase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 10, false, false));

            selfEntity.setPosition((double) telePos.getX() + 0.5D, (double) telePos.getY() + 1.25D, (double) telePos.getZ() + 0.5D);
        }
    }




    public boolean teleportConditions(double atX, double atY, double atZ)
    {
        EntityHostII self = (EntityHostII) (Object) this;
        Entity selfEntity = (Entity) (Object) this; 
        EntityLiving selfLiving = (EntityLiving) (Object) this; 


//If has target and target not very close
        EntityLivingBase target = selfLiving.getAttackTarget();     
        if (target != null && target.getDistanceSq(selfEntity) > 64.0D)
        {
//If this is burrowed
            if (!self.getBurrowed() && selfEntity.height <= 0.25F) 
            {
//Get nearest ground blockpos from teleport location
                BlockPos telePos = BlockUtil.findFirstSolidBlock
                    (selfEntity.world, (int) atX, (int) atY, (int) atZ, 
                    1.0F, 10, 1);
//If found a valid one
                if(telePos != null)
                {
//Then can teleport
                    return true;
                }
            }
        }


        return false;
    }

}
