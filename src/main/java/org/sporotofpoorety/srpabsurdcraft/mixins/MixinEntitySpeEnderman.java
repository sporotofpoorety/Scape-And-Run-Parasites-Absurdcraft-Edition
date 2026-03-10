package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;


import javax.annotation.Nullable;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;


import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.infected.special.EntitySpeEnderman;




//Mixin this class
@Mixin(value = EntitySpeEnderman.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinEntitySpeEnderman
{

    @Shadow(remap = false) protected abstract boolean teleportTo(double x, double y, double z);


//No telefrag
    @WrapMethod(method = "teleportToPos", remap = false, require = 1)
    protected boolean endermanAssimaraTeleportOverride(double x, double y, double z, double dis, Operation<Boolean> original) 
    {
        EntitySpeEnderman self = (EntitySpeEnderman) (Object) this;
        Entity selfEntity = (Entity) (Object) this;

        double radius = 32.0D + (8.0D * selfEntity.world.rand.nextDouble());
        double angle = selfEntity.world.rand.nextDouble() * (2.0D * Math.PI);

        double xNew = x + radius * Math.cos(angle);
        double zNew = z + radius * Math.sin(angle);

        return this.teleportTo(xNew, y, zNew);
    }




    @Inject
    (
//      method = "teleportTo",
        method = "teleportTo(DDD)Z",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
//Never teleport too close to target
    private void endermanAssimaraNeverTelefrag(double x, double y, double z, CallbackInfoReturnable<Boolean> callInfo) 
    {
        EntitySpeEnderman self = (EntitySpeEnderman) (Object) this;
        Entity selfEntity = (Entity) (Object) this;
        EntityLiving selfEntityLiving = (EntityLiving) (Object) this;


        EntityLivingBase attackTarget = selfEntityLiving.getAttackTarget();


//If has target
        if(attackTarget != null)
        {
//Get horizontal distance of teleport position to target
            double targetTeleDistX = x - attackTarget.posX;
            double targetTeleDistZ = z - attackTarget.posZ;
            double targetTeleDist = Math.sqrt(targetTeleDistX * targetTeleDistX + targetTeleDistZ * targetTeleDistZ);

//If teleporting too close
            if(targetTeleDist < 32.0D)
            {
//Stop
                callInfo.setReturnValue(false);
                callInfo.cancel();
            }       
        }
    }


    @Inject
    (
//      method = "teleportTo",
        method = "teleportTo(Lcom/dhanantry/scapeandrunparasites/entity/ai/misc/EntityParasiteBase;DDD)Z",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
//Never teleport too close to target
    private void endermanAssimaraNeverTelefragWithParasite(EntityParasiteBase in, double x, double y, double z, CallbackInfoReturnable<Boolean> callInfo) 
    {
        EntitySpeEnderman self = (EntitySpeEnderman) (Object) this;
        Entity selfEntity = (Entity) (Object) this;
        EntityLiving selfEntityLiving = (EntityLiving) (Object) this;


        EntityLivingBase attackTarget = selfEntityLiving.getAttackTarget();


//If has target
        if(attackTarget != null)
        {
//Get horizontal distance of teleport position to target
            double targetTeleDistX = x - attackTarget.posX;
            double targetTeleDistZ = z - attackTarget.posZ;
            double targetTeleDist = Math.sqrt(targetTeleDistX * targetTeleDistX + targetTeleDistZ * targetTeleDistZ);

//If teleporting too close
            if(targetTeleDist < 32.0D)
            {
//Stop
                callInfo.setReturnValue(false);
                callInfo.cancel();
            }       
        }
    }



/*
    @Inject
    (
        method = "onLivingUpdate()V",
        at = @At("TAIL"),
        require = 1,
        remap = true
    )
//Crawls dynamically
    private void endermanAssimaraCrawlingDynamically(CallbackInfo callInfo) 
    {
        EntityFerEnderman self = (EntityFerEnderman) (Object) this;
        Entity selfEntity = (Entity) (Object) this;


//Every second
        if((selfEntity.ticksExisted % 20) == 0)
        {
            EntityLiving selfEntityLiving = (EntityLiving) (Object) this;
            EntityLivingBase attackTarget = selfEntityLiving.getAttackTarget();


//If has target but can't path to target, crawl
            if(attackTarget != null)
            {
                boolean hasPathToTarget = (selfEntityLiving.getNavigator().getPathToEntityLiving(attackTarget) != null);

                self.setCrawling(!hasPathToTarget);
            }
        }
    }
*/
}
