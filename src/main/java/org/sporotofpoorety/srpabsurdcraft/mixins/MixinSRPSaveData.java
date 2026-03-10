package org.sporotofpoorety.srpabsurdcraft.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


import java.util.ArrayList;


import javax.annotation.Nullable;


import net.minecraft.entity.Entity;


import com.dhanantry.scapeandrunparasites.world.SRPSaveData;




@Mixin(value = SRPSaveData.class, remap = false)
//Abstract since mixins should not be instantiated
public abstract class MixinSRPSaveData
{

    @Shadow
    private ArrayList<Integer> dimEPid;


    @Inject
    (
        method = "getGeneModi2",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void changeGenerationValues(int id, CallbackInfoReturnable<Object> callInfo) 
    {
        SRPSaveData self = (SRPSaveData) (Object) this;


        float[] loot = new float[]{0.0F};

        for(int i = 0; i < this.dimEPid.size(); ++i) 
        {
            if ((Integer)this.dimEPid.get(i) == id) 
            {
/*
                switch((Byte)this.dimGeneration.get(i)) 
                {
                    case 0:
                    return new float[]{SRPConfigSystems.generationPoisonHeal0, SRPConfigSystems.generationMobHealing0, SRPConfigSystems.generationAttackSpeed0};
                    case 1:
                    return new float[]{SRPConfigSystems.generationPoisonHeal1, SRPConfigSystems.generationMobHealing1, SRPConfigSystems.generationAttackSpeed1};
                    case 2:
                    return new float[]{SRPConfigSystems.generationPoisonHeal2, SRPConfigSystems.generationMobHealing2, SRPConfigSystems.generationAttackSpeed2};
                    case 3:
                    return new float[]{SRPConfigSystems.generationPoisonHeal3, SRPConfigSystems.generationMobHealing3, SRPConfigSystems.generationAttackSpeed3};
                    case 4:
                    return new float[]{SRPConfigSystems.generationPoisonHeal4, SRPConfigSystems.generationMobHealing4, SRPConfigSystems.generationAttackSpeed4};
                    case 5:
                    return new float[]{SRPConfigSystems.generationPoisonHeal5, SRPConfigSystems.generationMobHealing5, SRPConfigSystems.generationAttackSpeed5};
                    default:
                    return new float[]{2.5F, 3.0F, 0.5F};
                }
*/


//              return new float[]{0.0F, 1.0F, 1.2F};
                callInfo.setReturnValue(new float[]{0.0F, 1.0F, 1.2F});
                callInfo.cancel();
            }
        }


        callInfo.setReturnValue(loot);
        callInfo.cancel();
    }


    @Inject
    (
        method = "getChoice",
        at = @At("HEAD"),
        cancellable = true,
        require = 1
    )
    private void difficultyIsNew(CallbackInfoReturnable<Integer> callInfo) 
    {
        callInfo.setReturnValue(1);
        callInfo.cancel();
    }

}
