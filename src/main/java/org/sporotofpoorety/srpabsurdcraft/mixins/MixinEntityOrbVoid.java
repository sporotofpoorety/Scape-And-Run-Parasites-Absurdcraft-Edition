package org.sporotofpoorety.srpabsurdcraft.mixins;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import javax.annotation.Nullable;


import net.minecraft.nbt.NBTTagCompound;


import com.dhanantry.scapeandrunparasites.entity.EntityOrbVoid;


import org.sporotofpoorety.srpabsurdcraft.entity.EntityOrbVoidCustom;


import org.sporotofpoorety.srpabsurdcraft.interfacemixins.IMixinEntityOrbVoid;




//Lemme write down how the original 
//logic flow works to try to make sense of it

//Timers:
//ticksExisted
//timeSinceIgnited
//timerDDD

//Lengths:
//StartState/WAITSTART 
//FuseState/FUSE       (Grows during this)
//Hardcoded


//Functions:
//onUpdate else
//onUpdate main + orbDoing() + setSelfeState(1) + dyingBurst(true, 1) (Else branch)
//selfExplode (called from dyingBurst() main branch)




@Mixin(value = EntityOrbVoid.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinEntityOrbVoid implements IMixinEntityOrbVoid
{

    @Shadow private double poosX;
    @Shadow private double poosY;
    @Shadow private double poosZ;


//Boolean for if custom
    @Unique private boolean orbVoidIsAbsurdcraft;




//No "orbDoing" if custom orb
    @WrapWithCondition
    (
        method = "onUpdate",
        at = 
        @At
        (
            value = "INVOKE",
            target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityOrbVoid;orbDoing()V"
        )
    )
    private boolean overrideOrbVoidBehavior(EntityOrbVoid self) 
    {
        return !this.getOrbVoidIsAbsurdcraft();
    }




//Action for when orb starts growing
    public void whenOrbStartsGrowing()
    {

    }



//New getters


    public boolean getOrbVoidIsAbsurdcraft()
    {
        return this.orbVoidIsAbsurdcraft;
    }


//New setters


    public void setOrbVoidIsAbsurdcraft(boolean isAbsurdcraft)
    {
        this.orbVoidIsAbsurdcraft = isAbsurdcraft;
    }

    public void setOrbPoos(double x, double y, double z)
    {
        this.poosX = x;
        this.poosY = y;
        this.poosZ = z;
    }


    @Inject
    (
        method = "writeEntityToNBT",
        at = @At("TAIL")
    )
    private void writeNewNBT(NBTTagCompound compound, CallbackInfo callInfo)
    {
//New NBT
        compound.setBoolean("OrbVoidIsAbsurdcraft", this.getOrbVoidIsAbsurdcraft());
    }


    @Inject
    (
        method = "readEntityFromNBT",
        at = @At("TAIL")
    )
    private void readNewNBT(NBTTagCompound compound, CallbackInfo callInfo)
    {
//New NBT
        if (compound.hasKey("OrbVoidIsAbsurdcraft")) {
            this.setOrbVoidIsAbsurdcraft(compound.getBoolean("OrbVoidIsAbsurdcraft"));
        }
    }

}

