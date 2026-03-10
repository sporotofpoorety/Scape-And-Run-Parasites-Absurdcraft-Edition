package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import javax.annotation.Nullable;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;


import com.dhanantry.scapeandrunparasites.potion.SRPEffectBase;




@Mixin(value = SRPEffectBase.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinSRPEffectBase
{

    @Inject
    (
        method = "effectCOTH",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void playerNoCothSpread(EntityLivingBase entity, int amplifier, CallbackInfo callInfo) 
    {
        if(entity instanceof EntityPlayer)
        {
            callInfo.cancel();
        }
    }


    @Inject
    (
        method = "effectCOTHextendDuration",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void playerNoCothExtend(int duration, int durationCheck, EntityLivingBase entity, int amplifier, 
    boolean flagPr, boolean origin, boolean evoPoints, CallbackInfo callInfo) 
    {
        if(entity instanceof EntityPlayer)
        {
            callInfo.cancel();
        }
    }

}

