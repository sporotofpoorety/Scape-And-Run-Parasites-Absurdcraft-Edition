package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import net.minecraft.entity.EntityLivingBase;


import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;


import org.sporotofpoorety.srpabsurdcraft.interfacemixins.IMixinEntityParasiteBase;




//Mixin this class
@Mixin(value = EntityParasiteBase.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinEntityParasiteBase implements IMixinEntityParasiteBase
{

    @Shadow protected int attackSpeedT;
    @Shadow protected boolean canD;

    @Shadow(remap = false) protected abstract void selfExplode();

    @Inject
    (
        method = "attackEntityAsMobMinimum",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void cancelMinimumDamage(EntityLivingBase target, float MinimumDamage, CallbackInfoReturnable<Boolean> callInfo)
    {
        callInfo.setReturnValue(false);
        callInfo.cancel();
    }


    @Inject
    (
        method = "getAttackSpeed",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void attackSpeed(CallbackInfoReturnable<Integer> callInfo)
    {
        callInfo.setReturnValue( (int) ( (float)this.attackSpeedT * 1.2F ) );
        callInfo.cancel();
    }


    @Inject
    (
//onLivingUpdate
        method = "func_70636_d",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void teleportDelay(CallbackInfo callInfo)
    {

    }


    public void selfExplodePublic()
    {
        EntityParasiteBase self = (EntityParasiteBase) (Object) this;

        this.selfExplode();
    }


    public boolean getCanDespawn()
    {
        return this.canD;
    }

}
