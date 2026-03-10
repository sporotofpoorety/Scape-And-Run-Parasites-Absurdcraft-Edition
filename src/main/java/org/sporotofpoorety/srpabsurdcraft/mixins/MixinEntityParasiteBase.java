package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import javax.annotation.Nullable;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;


import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;




//Mixin this class
@Mixin(value = EntityParasiteBase.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinEntityParasiteBase
{


    @Inject
    (
        method = "attackEntityAsMobMinimum",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelMinimumDamage(EntityLivingBase target, float MinimumDamage, CallbackInfoReturnable<Boolean> callInfo)
    {
        callInfo.setReturnValue(false);
        callInfo.cancel();
    }


/*
    @Inject
    (
        method = "onLivingUpdate",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void teleportDelay(CallbackInfo callInfo)
    {

    }
*/

}
