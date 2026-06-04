package org.sporotofpoorety.srpabsurdcraft.mixins;


import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.Random;



import javax.annotation.Nullable;


import org.objectweb.asm.Opcodes;


import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;


import com.dhanantry.scapeandrunparasites.entity.EntityOrbVoid;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;


import org.sporotofpoorety.srpabsurdcraft.entity.EntityOrbVoidCustom;
import org.sporotofpoorety.srpabsurdcraft.interfacemixins.IMixinEntityOrbVoid;




//Lemme write down how the original 
//logic flow works to try to make sense of it


//Timers:

//ticksExisted (now realTicksExisted)
//timeSinceIgnited
//timerDDD


//Lengths:

//StartState/WAITSTART 
//FuseState/FUSE       (Grows during this)
//Hardcoded (now orbDeflatesWhen and orbDiesWhen)


//Functions:

//onUpdate else
//onUpdate main + orbDoing() + setSelfeState(1) + dyingBurst()'s else branch(true, 1)
//selfExplode (called from dyingBurst()'s main branch)




@Mixin(value = EntityOrbVoid.class, remap = false)
public abstract class MixinEntityOrbVoid implements IMixinEntityOrbVoid
{

    @Shadow EntityPMalleable father;
    @Shadow private double poosX;
    @Shadow private double poosY;
    @Shadow private double poosZ;
    @Shadow private boolean followF;
    @Shadow protected int timeSinceIgnited;
    @Shadow protected int timerDDD;

    @Shadow protected abstract void dyingBurst(boolean fromDeath, int value);
    @Shadow protected abstract void orbDoing();


//Boolean for if custom
    @Unique private boolean orbVoidIsAbsurdcraft;
    @Unique private int realTicksExisted;

//Real ticks existed
    @Unique private static final DataParameter<Integer> REAL_TICKS_EXISTED = EntityDataManager.<Integer>createKey(EntityOrbVoid.class, DataSerializers.VARINT);
    @Unique private static final DataParameter<Float> GROWTH_RATE = EntityDataManager.<Float>createKey(EntityOrbVoid.class, DataSerializers.FLOAT);
    @Unique private static final DataParameter<Float> DEFLATE_RATE = EntityDataManager.<Float>createKey(EntityOrbVoid.class, DataSerializers.FLOAT);




    @Inject
    (
//entityInit()
        method = "func_70088_a",
        at = @At("TAIL"),
        require = 1
    )
//On entity init
    private void entityInitNewDataParameters(CallbackInfo callInfo)
    {
        Entity selfEntity = (Entity) (Object) this;
//Register the new data parameters
        selfEntity.getDataManager().register(REAL_TICKS_EXISTED, Integer.valueOf(0));
        selfEntity.getDataManager().register(GROWTH_RATE, Float.valueOf(2.0F));
        selfEntity.getDataManager().register(DEFLATE_RATE, Float.valueOf(1.0F));
    }




//No "orbDoing" if custom orb
    @WrapWithCondition
    (
//onUpdate()
        method = "func_70071_h_",         
        at = 
        @At
        (
            value = "INVOKE",
            target = "Lcom/dhanantry/scapeandrunparasites/entity/EntityOrbVoid;orbDoing()V"
        ),
        require = 1
    )
    private boolean overrideOrbVoidBehavior(EntityOrbVoid self) 
    {
        return !this.getOrbVoidIsAbsurdcraft();
    }


/*
    @Inject
    (
//onUpdate()
        method = "func_70071_h_",
        at = @At("HEAD"),
        require = 1
    )
    private void overrideTicksExisted(CallbackInfo callInfo)
*/
//Just gonna recreate the onUpdate() at this point


    /**
     * @author sporotofpoorety
     * @reason Need to fully rework black hole logic
     */
//  @Overwrite
//  public void func_70071_h_()
//  {
//      super.onUpdate();
//      ((net.minecraft.entity.Entity) (Object) this).onUpdate();


    @Inject
    (
//onUpdate()
        method = "func_70071_h_",
        at = 
        @At
        (
            value = "FIELD",
            opcode = Opcodes.GETFIELD, 
            ordinal = 0,
            shift = At.Shift.BEFORE
        ),
        cancellable = true,
        require = 1
    )
    private void overrideTicksExisted(CallbackInfo callInfo)
    {
        EntityOrbVoid self = (EntityOrbVoid) (Object) this;
        Entity selfEntity = (Entity) (Object) this;


        if (this.realTicksExisted > self.getStartState()) 
        {
/*
            this.orbDoing();
            self.setSelfeState(1);
            this.dyingBurst(true, 1);
*/
            
            
            if (selfEntity.world.isRemote) 
            {
                for(int i = 0; i < 4; ++i) 
                {
                    selfEntity.world.spawnParticle(EnumParticleTypes.PORTAL, selfEntity.posX + (selfEntity.world.rand.nextDouble() - 0.5D) * (double)selfEntity.width * 3.0D, selfEntity.posY + selfEntity.world.rand.nextDouble() * (double)selfEntity.height - 0.25D, selfEntity.posZ + (selfEntity.world.rand.nextDouble() - 0.5D) * (double)selfEntity.width * 3.0D, (selfEntity.world.rand.nextDouble() - 0.5D) * 2.0D, -selfEntity.world.rand.nextDouble(), (selfEntity.world.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
                }

                if (this.father != null) 
                {
                   self.prevRenderYawOffset = this.father.prevRenderYawOffset;
                   self.renderYawOffset = this.father.renderYawOffset;
                   self.prevRotationYawHead = this.father.prevRotationYawHead;
                   self.rotationYawHead = this.father.rotationYawHead;
                }

                self.spawnOrbEffects(4);
                return;
            }

            if (this.father != null) 
            {
                if (this.father.isEntityAlive() && this.followF) 
                {
                    selfEntity.posX = this.father.posX;
                    selfEntity.posY = this.father.posY - selfEntity.world.rand.nextDouble() * 0.1D + (double)this.father.height + self.offsetOrb;
                    selfEntity.posZ = this.father.posZ;
                } 
                else 
                {
                    selfEntity.posX = this.poosX;
                    selfEntity.posY = this.poosY - selfEntity.world.rand.nextDouble() * 0.1D;
                    selfEntity.posZ = this.poosZ;
                    if (this.followF) 
                    {
                        selfEntity.setDead();
                    }
                }
            }  
/*
            else 
            {
                selfEntity.posX = this.poosX;
                selfEntity.posY = this.poosY - selfEntity.world.rand.nextDouble() * 0.1D;
                selfEntity.posZ = this.poosZ;
            }
*/
        } 
        else 
        {
            if (selfEntity.world.isRemote) 
            {
                self.spawnOrbEffects(4);
                return;
            }

            if (this.father != null) 
            {
                if (this.father.isEntityAlive() && this.followF) 
                {
                    this.poosX = this.father.posX;
                    this.poosY = this.father.posY + (double)this.father.height + self.offsetOrb;
                    this.poosZ = this.father.posZ;
                    selfEntity.posX = this.father.posX;
                    selfEntity.posY = this.father.posY - selfEntity.world.rand.nextDouble() * 0.1D + (double)this.father.height + self.offsetOrb;
                    selfEntity.posZ = this.father.posZ;
                } 
                else 
                {
                    if (this.followF) 
                    {
                        selfEntity.setDead();
                    }

                    this.poosX = selfEntity.posX;
                    this.poosY = selfEntity.posY;
                    this.poosZ = selfEntity.posZ;
                }
            }
/* 
            else 
            {
                this.poosX = selfEntity.posX;
                this.poosY = selfEntity.posY;
                this.poosZ = selfEntity.posZ;
            }
*/
        }

        
        callInfo.cancel();
    }




//Action for when orb starts growing
    public void whenOrbStartsGrowing()
    {

    }




//New getters

    public int getRealTicksExisted()
    {
        Entity selfEntity = (Entity) (Object) this;
        return ((Integer)selfEntity.getDataManager().get(REAL_TICKS_EXISTED)).intValue();
    }
    public int getRealTicksExistede()
    {
        return this.realTicksExisted;
    }

    public float getGrowthRate()
    {
        Entity selfEntity = (Entity) (Object) this;
        return ((Float)selfEntity.getDataManager().get(GROWTH_RATE)).floatValue();
    }

    public float getDeflateRate()
    {
        Entity selfEntity = (Entity) (Object) this;
        return ((Float)selfEntity.getDataManager().get(DEFLATE_RATE)).floatValue();
    }

    public boolean getOrbVoidIsAbsurdcraft()
    {
        return this.orbVoidIsAbsurdcraft;
    }


//New setters

    public void setRealTicksExisted(int realTicks)
    {
        Entity selfEntity = (Entity) (Object) this;
        selfEntity.getDataManager().set(REAL_TICKS_EXISTED, Integer.valueOf(realTicks));    
    }
    public void setRealTicksExistede(int realTicks)
    {
        this.realTicksExisted = realTicks;
    }

    public void setGrowthRate(float growth)
    {
        Entity selfEntity = (Entity) (Object) this;
        selfEntity.getDataManager().set(GROWTH_RATE, Float.valueOf(growth));    
    }

    public void setDeflateRate(float deflate)
    {
        Entity selfEntity = (Entity) (Object) this;
        selfEntity.getDataManager().set(DEFLATE_RATE, Float.valueOf(deflate));    
    }

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

}

