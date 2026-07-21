package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.ArrayList;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;


import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.monster.primitive.EntityShyco;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;


import org.sporotofpoorety.eternitymode.entity.EntityExplosiveShockwave;
import org.sporotofpoorety.eternitymode.util.AimUtil;
import org.sporotofpoorety.eternitymode.util.DirectionalSpreadUtil;




//Mixin this class
@Mixin(value = EntityShyco.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinEntityShyco 
{

    @Shadow private float attackTimer;
    @Shadow private boolean up;
    @Shadow private int border;
    @Shadow private boolean skillshockwave;
    @Shadow protected abstract void spawnShock();



    @Inject
    (
        method = "shockwave",
        at = @At("HEAD"),
        cancellable = true
    )
//Simpler conditions for shockwave
    private void overridePriLongarmsShockwaveConditions(CallbackInfo callInfo) 
    {
        EntityShyco self = (EntityShyco) (Object) this;
        Entity selfEntity = (Entity) (Object) this;
        EntityLiving selfEntityLiving = (EntityLiving) (Object) this;
        EntityParasiteBase selfParasiteBase = (EntityParasiteBase) (Object) this;


//IDK what this is but ok SRP
        selfParasiteBase.setParasiteStatus(10);
//Clear navigation
        selfEntityLiving.getNavigator().clearPath();
//Border is "attack state" but confusingly named
        if (this.border == 0) 
        {
            float v = (selfEntity.world.rand.nextFloat() - selfEntity.world.rand.nextFloat()) * 0.4F + 2.0F;
            selfEntity.playSound(SRPSounds.SHYCO_HURT, 4.0F, v);
            ++this.border;
        } 
        else 
        {
            if (this.border <= 1) 
            {
//Idk what this is
                selfEntity.world.setEntityState(selfEntity, (byte)100);
            }


//Every half-second 
            if (selfEntity.ticksExisted % 10 == 0) 
            {
//Increment attack step
                ++this.border;
//Or, if target null
                if (selfEntityLiving.getAttackTarget() == null) 
                {
//Reset attack state
                    this.skillshockwave = true;
                    selfParasiteBase.setParasiteStatus(0);
                    this.border = 0;
                } 
                else 
                {
//At max border
                    if (this.border == 2) 
                    {
//Shoot shockwave
                        this.spawnShock();
                        this.up = true;
                        this.attackTimer = 0.0F;
                        selfEntity.world.setEntityState(selfEntity, (byte)12);
                        selfEntity.playSound(SRPSounds.SWIPE, 2.0F, 1.0F);
                    }

                    if (this.border > 4) 
                    {
//Reset attack state
                        this.skillshockwave = true;
                        selfParasiteBase.setParasiteStatus(0);
                        this.border = 0;
                    }
                } 
            }
        }

        callInfo.cancel();
    }


    @Inject
    (
        method = "spawnShock",
        at = @At("HEAD"),
        cancellable = true
    )
//Different shockwave entirely
    private void overridePriLongarmsShockwave(CallbackInfo callInfo) 
    {
        EntityShyco self = (EntityShyco) (Object) this;
        Entity selfEntity = (Entity) (Object) this;

        if(!selfEntity.world.isRemote)
        {
            EntityLiving selfEntityLiving = (EntityLiving) (Object) this;

            EntityLivingBase attackTarget = selfEntityLiving.getAttackTarget();


            if (attackTarget != null)
            {
//Predictive aim
                Vec3d predictiveAim 
                    = AimUtil.calcPredictiveAimDynamicVertical
                    (new Vec3d(attackTarget.posX - selfEntity.posX, attackTarget.posY - selfEntity.posY, attackTarget.posZ - selfEntity.posZ), 
                    attackTarget, 2.0D, true, 64.0D);


                ArrayList<Vec3d> coneVecs = DirectionalSpreadUtil.shootCone(predictiveAim, 0.17 * Math.PI, 8);

                for(Vec3d coneVec : coneVecs)
                {
                    EntityExplosiveShockwave shockwave = new EntityExplosiveShockwave(selfEntity.world, selfEntity.posX, selfEntity.posY + 2.5D, selfEntity.posZ, 
                        self, 
                        50, false, 3.0F, 
                        coneVec.x, coneVec.y, coneVec.z, 
                        1.0D,
                        false, 3.0D, 15, 
                        2, 3.0F, 13.0F,
                        false, 0.0D, false, 0, 69420);

	                selfEntity.getEntityWorld().spawnEntity(shockwave);
                }
/*
                double baseRadians = Math.atan2(predictiveAim.z, predictiveAim.x);


                for(int angleAt = -1; angleAt <= 1; angleAt++)
                {
                    EntityExplosiveShockwave shockwave = new EntityExplosiveShockwave(selfEntity.world, selfEntity.posX, selfEntity.posY + 2.5D, selfEntity.posZ, 
                        self, 
                        50, false, 3.0F, 
                        2.0D * Math.cos(baseRadians + (Math.PI * 0.25D * angleAt)), predictiveAim.y, 2.0D * Math.sin(baseRadians + (Math.PI * 0.25D * angleAt)), 
                        1.0D,
                        false, 3.0D, 15, 
                        2, 3.0F, 13.0F,
                        false, 0.0D, false, 0, 69420);

	                selfEntity.getEntityWorld().spawnEntity(shockwave);
                }
*/
            }
        } 


        callInfo.cancel();
    }
}
