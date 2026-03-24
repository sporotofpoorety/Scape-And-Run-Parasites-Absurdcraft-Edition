package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import net.minecraft.util.math.AxisAlignedBB;


import com.dhanantry.scapeandrunparasites.entity.ai.EntityAIDodAttack;




//Mixin this class
@Mixin(value = EntityAIDodAttack.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinEntityAIDodAttack
{

/*
    @Inject
    (
        method = "unhide",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void stopUnhide(AxisAlignedBB axisalignedbb, CallbackInfo callInfo)
    {
        callInfo.cancel();
    }
*/

}
