package org.sporotofpoorety.srpabsurdcraft.entity;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;


import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import com.dhanantry.scapeandrunparasites.client.particle.ParticleSpawner;
import com.dhanantry.scapeandrunparasites.client.particle.SRPEnumParticle;
import com.dhanantry.scapeandrunparasites.entity.EntityOrbVoid;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPCosmical;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPMalleable;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPPreeminent;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.init.SRPSounds;


import org.sporotofpoorety.eternitymode.core.EternityModeSoundEvents;
import org.sporotofpoorety.eternitymode.entity.EntityThrownBlock;
import org.sporotofpoorety.eternitymode.packets.ExplosionVisualPacket;
import org.sporotofpoorety.eternitymode.util.BlockUtil;
import org.sporotofpoorety.eternitymode.util.DirectionalSpreadUtil;
import org.sporotofpoorety.eternitymode.util.PacketUtil;
import org.sporotofpoorety.eternitymode.util.EntityBlockData;
import org.sporotofpoorety.eternitymode.util.ExplosionUtil;

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




public class EntityOrbVoidCustom extends EntityOrbVoid 
{
    public IMixinEntityOrbVoid orbVoidMixin;
    public boolean orbFreeMoving = true;


//Owner that doesn't have to be a parasite
    public EntityLiving owner;
    public UUID ownerUUID;
    public boolean previousValidateOwnerFailed;


//Orb type
    public String orbCustomType = "none";

//Misc presentation
    public boolean dontVisualExplosion;
    public boolean dontSoundGrowing;
    public boolean dontSoundActive;
    public boolean dontSoundWarning;
    public boolean dontSoundExplosion;


//This is necessary to keep or change size with differing timers
/*
    public float growthRate = 1.0F;
    public float deflateRate = 1.0F;
*/


//Orb death timer not hardcoded
    public int orbDeflatesWhen = 80;
    public int orbDiesWhen = 90;


//List of blocks
    public List<EntityBlockData> orbBlocks = new ArrayList<>();


//Block expel
    public double blockForceHorizontal;
    public double blockForceVertical;
    public double blockForceGeneral;
    public double blockAcceleration = 1.0D;
    public float blockDamage = 1.0F;

    public int fountainBlockRate = 3;
    public int scatterBlockCount = 100;
    public int aimedBlockCount = 100;
    public boolean pullDontDestroy;
    public int pullSearchDepth = 32;


//Shower orb specific
    public double riseSpeed;
    public double riseLimit;


//Homing fountain orb specific
    public double homingFactor;
    public boolean homingSnapEnabled; 
    public boolean homingSnapVertical; 
    public double homingSnapThreshold;
    public double homingSnapSteps;




    public EntityOrbVoidCustom(World worldIn) 
    {
        super(worldIn);

        this.setCustomOrbVoid();
    }

    public EntityOrbVoidCustom(World worldIn, EntityPMalleable in, EntityLiving owner, int fuse, int waitStart,
    float growthRate, float deflateRate, int orbDeflatesWhen, int orbDiesWhen) 
    {
        super(worldIn);

        this.setFuseState(fuse);
        this.setStartState(waitStart);

        this.setCustomOrbVoid();

        this.owner = owner;
        if(this.owner != null) { this.ownerUUID = owner.getUniqueID(); }

/*
        this.growthRate = growthRate;
        this.deflateRate = deflateRate;
*/
        this.orbVoidMixin.setGrowthRate(growthRate);
        this.orbVoidMixin.setDeflateRate(deflateRate);

        this.orbDeflatesWhen = orbDeflatesWhen;
        this.orbDiesWhen = orbDiesWhen;
    }

    public EntityOrbVoidCustom(World worldIn, EntityPMalleable in, EntityLiving owner, int fuse, int waitStart, boolean stayPY,
    float growthRate, float deflateRate, int orbDeflatesWhen, int orbDiesWhen) 
    {
        super(worldIn);

        this.setFuseState(fuse);
        this.setStartState(waitStart);

        this.setCustomOrbVoid();

        this.owner = owner;
        if(this.owner != null) { this.ownerUUID = owner.getUniqueID(); }

/*
        this.growthRate = growthRate;
        this.deflateRate = deflateRate;
*/
        this.orbVoidMixin.setGrowthRate(growthRate);
        this.orbVoidMixin.setDeflateRate(deflateRate);

        this.orbDeflatesWhen = orbDeflatesWhen;
        this.orbDiesWhen = orbDiesWhen;
    }

    public void setCustomOrbVoid()
    {
        this.orbVoidMixin = (IMixinEntityOrbVoid) this;

        this.orbVoidMixin.setOrbVoidIsAbsurdcraft(true);
    }

    public void setOrbShower(double blockForceHorizontal, double blockForceVertical, double blockForceGeneral, double blockAcceleration, float blockDamage,
    int scatterBlockCount, int aimedBlockCount,
    double riseSpeed, double riseLimit)
    {
        this.blockForceHorizontal = blockForceHorizontal;
        this.blockForceVertical = blockForceVertical;
        this.blockForceGeneral = blockForceGeneral;
        this.blockAcceleration = blockAcceleration;
        this.blockDamage = blockDamage;

        this.scatterBlockCount = scatterBlockCount;
        this.aimedBlockCount = aimedBlockCount;

        this.riseSpeed = riseSpeed;
        this.riseLimit = riseLimit;
    }

    public void setOrbFountain(double blockForceHorizontal, double blockForceVertical, double blockForceGeneral, double blockAcceleration, float blockDamage,
    int fountainBlockRate, int scatterBlockCount, int aimedBlockCount,
    double homingFactor, boolean homingSnapEnabled, boolean homingSnapVertical, double homingSnapThreshold, double homingSnapSteps)
    {
        this.blockForceHorizontal = blockForceHorizontal;
        this.blockForceVertical = blockForceVertical;
        this.blockForceGeneral = blockForceGeneral;
        this.blockAcceleration = blockAcceleration;
        this.blockDamage = blockDamage;

        this.fountainBlockRate = fountainBlockRate;
        this.scatterBlockCount = scatterBlockCount;
        this.aimedBlockCount = aimedBlockCount;

        this.homingFactor = homingFactor;
        this.homingSnapEnabled = homingSnapEnabled;
        this.homingSnapVertical = homingSnapVertical;
        this.homingSnapThreshold = homingSnapThreshold;
        this.homingSnapSteps = homingSnapSteps;
    }

    public void writeEntityToNBT(NBTTagCompound compound) 
    {
//      super.writeEntityToNBT(compound);


        compound.setInteger("RealTicksExisted", this.orbVoidMixin.getRealTicksExistede());
        compound.setInteger("TimeSinceIgnited", this.timeSinceIgnited);
        compound.setInteger("TimerDDD", this.timerDDD);

//idk what this one does but yeah
        compound.setInteger("LastActiveTime", this.lastActiveTime);

        compound.setBoolean("OrbVoidIsAbsurdcraft", this.orbVoidMixin.getOrbVoidIsAbsurdcraft());


        compound.setBoolean("OrbFreeMoving", this.orbFreeMoving);

        if (compound.hasUniqueId("OwnerUUID")) 
        { 
            this.ownerUUID = compound.getUniqueId("OwnerUUID"); 
            this.validateOwner();
        }

        compound.setString("OrbCustomType", this.orbCustomType);

        compound.setBoolean("DontVisualExplosion", this.dontVisualExplosion);
        compound.setBoolean("DontSoundGrowing", this.dontSoundGrowing);
        compound.setBoolean("DontSoundActive", this.dontSoundActive);
        compound.setBoolean("DontSoundWarning", this.dontSoundWarning);
        compound.setBoolean("DontSoundExplosion", this.dontSoundExplosion);

        compound.setFloat("GrowthRate", this.orbVoidMixin.getGrowthRate());
        compound.setFloat("DeflateRate", this.orbVoidMixin.getDeflateRate());

        compound.setInteger("OrbFuseState", this.getFuseState());
        compound.setInteger("OrbStartState", this.getStartState());

        compound.setInteger("OrbDeflatesWhen", this.orbDeflatesWhen);
        compound.setInteger("OrbDiesWhen", this.orbDiesWhen);

        this.nbtWriteBlockList(compound);       

        compound.setDouble("BlockForceHorizontal", this.blockForceHorizontal);
        compound.setDouble("BlockForceVertical", this.blockForceVertical);
        compound.setDouble("BlockForceGeneral", this.blockForceGeneral); 
        compound.setDouble("BlockAcceleration", this.blockAcceleration); 
        compound.setFloat("BlockDamage", this.blockDamage); 

        compound.setInteger("FountainBlockRate", this.fountainBlockRate);
        compound.setInteger("ScatterBlockCount", this.scatterBlockCount);
        compound.setInteger("AimedBlockCount", this.aimedBlockCount);
        compound.setBoolean("PullDontDestroy", this.pullDontDestroy);
        compound.setInteger("PullSearchDepth", this.pullSearchDepth);

        compound.setDouble("RiseSpeed", this.riseSpeed);
        compound.setDouble("RiseLimit", this.riseLimit);

        compound.setDouble("HomingFactor", this.homingFactor);
        compound.setBoolean("HomingSnapEnabled", this.homingSnapEnabled);
        compound.setBoolean("HomingSnapVertical", this.homingSnapVertical);
        compound.setDouble("HomingSnapThreshold", this.homingSnapThreshold);
        compound.setDouble("HomingSnapSteps", this.homingSnapSteps);
    }

    public void readEntityFromNBT(NBTTagCompound compound) 
    {
//      super.readEntityFromNBT(compound);

//Is nbt reading?
        if (compound.hasKey("RealTicksExisted")) { this.orbVoidMixin.setRealTicksExistede(compound.getInteger("RealTicksExisted")); }
        if (compound.hasKey("TimeSinceIgnited")) { this.timeSinceIgnited = compound.getInteger("TimeSinceIgnited"); }
        if (compound.hasKey("TimerDDD")) { this.timerDDD = compound.getInteger("TimerDDD"); }

//idk what this one does but yeah
        if (compound.hasKey("LastActiveTime")) { this.lastActiveTime = compound.getInteger("LastActiveTime"); }

        if (compound.hasKey("OrbVoidIsAbsurdcraft")) { this.orbVoidMixin.setOrbVoidIsAbsurdcraft(compound.getBoolean("OrbVoidIsAbsurdcraft")); }

        
        if (compound.hasKey("OrbFreeMoving")) { this.orbFreeMoving = compound.getBoolean("OrbFreeMoving"); }

//      public EntityLiving owner;

        if (compound.hasKey("OrbCustomType")) { this.orbCustomType = compound.getString("OrbCustomType"); }

        if (compound.hasKey("DontVisualExplosion")) { this.dontVisualExplosion = compound.getBoolean("DontVisualExplosion"); }
        if (compound.hasKey("DontSoundGrowing")) { this.dontSoundGrowing = compound.getBoolean("DontSoundGrowing"); }
        if (compound.hasKey("DontSoundActive")) { this.dontSoundActive = compound.getBoolean("DontSoundActive"); }
        if (compound.hasKey("DontSoundWarning")) { this.dontSoundWarning = compound.getBoolean("DontSoundWarning"); }
        if (compound.hasKey("DontSoundExplosion")) { this.dontSoundExplosion = compound.getBoolean("DontSoundExplosion"); }

        if (compound.hasKey("GrowthRate")) { this.orbVoidMixin.setGrowthRate(compound.getFloat("GrowthRate")); }
        if (compound.hasKey("DeflateRate")) { this.orbVoidMixin.setDeflateRate(compound.getFloat("DeflateRate")); }

        if (compound.hasKey("OrbFuseState")) { this.setFuseState(compound.getInteger("OrbFuseState")); }
        if (compound.hasKey("OrbStartState")) { this.setStartState(compound.getInteger("OrbStartState")); }

        if (compound.hasKey("OrbDeflatesWhen")) { this.orbDeflatesWhen = compound.getInteger("OrbDeflatesWhen"); }
        if (compound.hasKey("OrbDiesWhen")) { this.orbDiesWhen = compound.getInteger("OrbDiesWhen"); }

        this.nbtReadBlockList(compound);

        if (compound.hasKey("BlockForceHorizontal")) { this.blockForceHorizontal = compound.getDouble("BlockForceHorizontal"); }
        if (compound.hasKey("BlockForceVertical")) { this.blockForceVertical = compound.getDouble("BlockForceVertical"); }
        if (compound.hasKey("BlockForceGeneral")) { this.blockForceGeneral = compound.getDouble("BlockForceGeneral"); }
        if (compound.hasKey("BlockAcceleration")) { this.blockAcceleration = compound.getDouble("BlockAcceleration"); }
        if (compound.hasKey("BlockDamage")) { this.blockDamage = compound.getFloat("BlockDamage"); }

        if (compound.hasKey("FountainBlockRate")) { this.fountainBlockRate = compound.getInteger("FountainBlockRate"); }
        if (compound.hasKey("ScatterBlockCount")) { this.scatterBlockCount = compound.getInteger("ScatterBlockCount"); }
        if (compound.hasKey("AimedBlockCount")) { this.aimedBlockCount = compound.getInteger("AimedBlockCount"); }
        if (compound.hasKey("PullDontDestroy")) { this.pullDontDestroy = compound.getBoolean("PullDontDestroy"); }
        if (compound.hasKey("PullSearchDepth")) { this.pullSearchDepth = compound.getInteger("PullSearchDepth"); }

        if (compound.hasKey("RiseSpeed")) { this.riseSpeed = compound.getDouble("RiseSpeed"); }
        if (compound.hasKey("RiseLimit")) { this.riseLimit = compound.getDouble("RiseLimit"); }

        if (compound.hasKey("HomingFactor")) { this.homingFactor = compound.getDouble("HomingFactor"); }
        if (compound.hasKey("HomingSnapEnabled")) { this.homingSnapEnabled = compound.getBoolean("HomingSnapEnabled"); }
        if (compound.hasKey("HomingSnapVertical")) { this.homingSnapVertical = compound.getBoolean("HomingSnapVertical"); }
        if (compound.hasKey("HomingSnapThreshold")) { this.homingSnapThreshold = compound.getDouble("HomingSnapThreshold"); }
        if (compound.hasKey("HomingSnapSteps")) { this.homingSnapSteps = compound.getDouble("HomingSnapSteps"); }
    }

    public void nbtWriteBlockList(NBTTagCompound compound)
    {
//Puppet array to store
        NBTTagList blockListToStore = new NBTTagList();

//For each thrown block 
        for (EntityBlockData blockData : this.orbBlocks) 
        {
//Make puppet map
            NBTTagCompound blockDataToStore = new NBTTagCompound();
                blockDataToStore.setInteger("OriginPosX", blockData.blockOrigin.getX());
                blockDataToStore.setInteger("OriginPosY", blockData.blockOrigin.getY());
                blockDataToStore.setInteger("OriginPosZ", blockData.blockOrigin.getZ());


                Block basisBlock = blockData.basisState == null ? Blocks.AIR : blockData.basisState.getBlock();
                blockDataToStore.setByte("Data", (byte)basisBlock.getMetaFromState(blockData.basisState));
                ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(basisBlock);
                blockDataToStore.setString("Block", resourcelocation == null ? "" : resourcelocation.toString());


                blockDataToStore.setBoolean("DontPlaceBlock", blockData.dontPlaceBlock);
                blockDataToStore.setBoolean("ShouldDropItem", blockData.shouldDropItem);
                blockDataToStore.setBoolean("DealsDamage", blockData.dealsDamage);
                blockDataToStore.setFloat("ThrownBlockDamage", blockData.thrownBlockDamage);
//Append it to puppet array
            blockListToStore.appendTag(blockDataToStore);
        }
        
        compound.setTag("BlockDataArray", blockListToStore); 
    }

    public void nbtReadBlockList(NBTTagCompound compound)
    {
//Check for block data list
        if (compound.hasKey("BlockDataArray")) 
        {
//It's an array of maps specifically
            NBTTagList storedBlockList = compound.getTagList("BlockDataArray", 10);

//For each stored block data
            for (int i = 0; i < storedBlockList.tagCount(); i++) 
            {
//Fetch data as compound
                NBTTagCompound storedBlock = storedBlockList.getCompoundTagAt(i);

//Recreate stored IBlockState 
//from metadata and resource location (please work)
                int storedMeta = storedBlock.getByte("Data") & 255;
                IBlockState storedState = Block.getBlockFromName(storedBlock.getString("Block")).getStateFromMeta(storedMeta); 


//Make corresponding entity thrown block
                EntityBlockData blockData = new EntityBlockData
                (
                    new BlockPos(storedBlock.getInteger("OriginPosX"), storedBlock.getInteger("OriginPosY"), storedBlock.getInteger("OriginPosZ")),
                    storedState,
                    storedBlock.getBoolean("DontPlaceBlock"),
                    storedBlock.getBoolean("ShouldDropItem"),
                    storedBlock.getBoolean("DealsDamage"),
                    storedBlock.getFloat("ThrownBlockDamage")
                );

//Store in the block data list
                this.orbBlocks.add(blockData);
            }
        }
    }




    public void onUpdate()
    {
//Increment real ticks existed
        this.orbVoidMixin.setRealTicksExistede(this.orbVoidMixin.getRealTicksExistede() + 1);


        int currentRealTicks = this.orbVoidMixin.getRealTicksExistede();
    
// DEBUG LOGGING
/*
        if (this.world.isRemote) 
        {
            if (currentRealTicks % 10 == 0) 
            {
                System.out.println("[CLIENT] RealTicks: " + currentRealTicks + 
                                   " | Fuse: " + this.getFuseState() +
                                   " | Start: " + this.getStartState() +
                                   " | TimeSinceIgnited: " + this.timeSinceIgnited +  
                                   " | TimerDDD: " + this.timerDDD + 
                                   " | DeflatesWhen: " + this.orbDeflatesWhen + 
                                   " | DiesWhen: " + this.orbDiesWhen + 
                                   " | SelfeState: " + this.getSelfeState());
            }
        } 
        else 
        {
            if (currentRealTicks % 10 == 0) 
            {
                System.out.println("[SERVER] RealTicks: " + currentRealTicks + 
                                   " | Fuse: " + this.getFuseState() +
                                   " | Start: " + this.getStartState() +  
                                   " | TimeSinceIgnited: " + this.timeSinceIgnited +  
                                   " | TimerDDD: " + this.timerDDD + 
                                   " | DeflatesWhen: " + this.orbDeflatesWhen + 
                                   " | DiesWhen: " + this.orbDiesWhen + 
                                   " | SelfeState: " + this.getSelfeState());
            }
        }
*/


//Periodically validate owner
        if(this.orbVoidMixin.getRealTicksExistede() % 20 == 0)
        {
//Checks for two failed validations in a row
            if(this.performOwnerValidation()) { this.previousValidateOwnerFailed = false; }
            else
            {
                this.previousValidateOwnerFailed = true;
            }
        }


        if(this.orbVoidMixin.getRealTicksExistede() > this.getStartState())
        {
            this.orbDoingCustom();
            this.setSelfeState(1);
            this.dyingBurst(true, 1);
        }


        super.onUpdate();


/*
        if(!this.world.isRemote)
        {
*/
//If at max pre-growth ticks
            if(this.orbVoidMixin.getRealTicksExistede() == this.getStartState())
            {
//Perform a function for start of growing
                this.whenOrbStartsGrowing();
            }


//If orb is active
            if (this.getSelfeState() == 2 && this.timerDDD <= this.orbDeflatesWhen)
            {
//Perform active function
                this.whenOrbIsActive();
            }


//Free orb movement
//            this.posX += this.motionX; this.posY += this.motionY; this.posZ += this.motionZ;
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            if(this.orbFreeMoving) { this.orbVoidMixin.setOrbPoos(this.posX, this.posY, this.posZ); }
//        } 
    }





    public void whenOrbStartsGrowing()
    {
        if(!this.dontSoundGrowing)
        {
            this.playSound(EternityModeSoundEvents.ENTITY_BLASTER_SOUND, 8.0F, 1.0F);
        }

        if(this.orbCustomType.equals("blockshower") || this.orbCustomType.equals("homingfountain"))
        {
            this.orbGrowingPull();
        }
    }


    public void orbGrowingPull()
    {
//Generate and return a ton of blocks
        ArrayList<EntityThrownBlock> generatedBlocks = BlockUtil.generateAndReturnRandomBlocks(this,
        this.owner, this.scatterBlockCount + this.aimedBlockCount, 32, this.pullSearchDepth, 2, !this.pullDontDestroy);


//For each one
//low lifetime, noclip and home into this
        for(EntityThrownBlock block : generatedBlocks)
        {
//Give them damage
            block.thrownBlockDamage = this.blockDamage;

            block.setBlockSolid(false);

//Gotta test if homing is precise
            int pullTime = this.getFuseState() - 2;
            block.setMovement(
                (this.posX - block.posX) / (double) pullTime, (this.posY - block.posY) / (double) pullTime, (this.posZ - block.posZ) / (double) pullTime, 
                0.0D, false, 1.0D);
            block.lifetimeMax = pullTime;

//Add each block's data to the list this has
            EntityBlockData blockData = new EntityBlockData(block.getOrigin(), block.getBasisState(), 
                block.dontPlaceBlock, block.shouldDropItem, block.dealsDamage, block.thrownBlockDamage);
            this.orbBlocks.add(blockData);


            if (!this.world.isRemote) { this.getEntityWorld().spawnEntity(block); }
        }

    }



    public void whenOrbIsActive()
    {
        if(this.orbCustomType.equals("blockshower"))
        {
            this.orbActiveShower();
        }
        if(this.orbCustomType.equals("homingfountain"))
        {
            this.orbActiveHomingFountain();
        }
    }


    public void orbActiveShower()
    {
        if(this.owner != null)
        {
            EntityLivingBase ownerTarget = owner.getAttackTarget();
//If owner has target
            if(ownerTarget != null)
            {
//If far above target then explode immediately
                if((this.posY - ownerTarget.posY) > this.riseLimit)
                {
                    this.setSelfeState(2);
                    this.timerDDD = this.orbDeflatesWhen;
                }
//Otherwise
                else
                {
//Ascend fast
                    this.motionY = this.riseSpeed;
                }
            }
        }

//If no valid owner explode immediately
        else
        {
            this.timerDDD = this.orbDeflatesWhen;
        }
    }


    public void orbActiveHomingFountain()
    {
        if(!this.world.isRemote && this.timerDDD < this.orbDeflatesWhen)
        {
//Spew blocks
            if(this.orbVoidMixin.getRealTicksExistede() > (this.getStartState() + this.getFuseState() + 2)
            && !this.orbBlocks.isEmpty())
            {
                for(int blockAt = 0; blockAt < this.fountainBlockRate; blockAt++)
                {
                    int randomBlockIndex = this.rand.nextInt(this.orbBlocks.size());
        
                    EntityBlockData blockData = this.orbBlocks.get(randomBlockIndex);

                    EntityThrownBlock block = new EntityThrownBlock(this.world, this.posX, this.posY, this.posZ, 
                        this.owner, blockData.basisState, 
                        true, true, true, blockData.thrownBlockDamage);
                    block.setOrigin(blockData.blockOrigin);
                    block.hasManualOrigin = true;
                    block.dontBreakInitialPos = true;

                    double shootRadian = this.rand.nextDouble() * 2.0D * Math.PI;
                    block.setMovement(this.rand.nextDouble() * Math.cos(shootRadian) * this.blockForceHorizontal, 
                        this.blockForceVertical * this.rand.nextDouble(), 
                        this.rand.nextDouble() * Math.sin(shootRadian) * this.blockForceHorizontal, 
                        0.08D, false, 1.0D);

                    this.world.spawnEntity(block);
                }
            }


//If owner not null
            if(this.owner != null)
            {
//And owner target not null
                EntityLivingBase ownerTarget = this.owner.getAttackTarget();
                if(ownerTarget != null)
                {
//Get distance
                    double targetDistX = ownerTarget.posX - this.posX;
                    double targetDistY = (ownerTarget.posY + 8.0D) - this.posY;
                    double targetDistZ = ownerTarget.posZ - this.posZ;

//Accelerate based on how far target is
                    double extraX = targetDistX * (0.0025D * this.homingFactor);
                    double extraY = targetDistY * (0.0025D * this.homingFactor);
                    double extraZ = targetDistZ * (0.0025D * this.homingFactor);

//Can snap to target if strayed too far
                    if(this.homingSnapEnabled)
                    {
                        if(this.homingSnapVertical)
                        {
                            if(Math.sqrt(targetDistX * targetDistX + targetDistY * targetDistY + targetDistZ * targetDistZ) >= this.homingSnapThreshold)
                            {
                                this.motionX = extraX * this.homingSnapSteps;
                                this.motionY = extraY * this.homingSnapSteps;
                                this.motionZ = extraZ * this.homingSnapSteps;
                            }
                        }
                        else
                        {
                            if(Math.sqrt(targetDistX * targetDistX + targetDistZ * targetDistZ) >= this.homingSnapThreshold)
                            {
                                this.motionX = extraX * this.homingSnapSteps;
                                this.motionY = extraY * this.homingSnapSteps;
                                this.motionZ = extraZ * this.homingSnapSteps;
                            }
                        }
                    }

//Stop previous movement if nearing time to explode
                    if(this.timerDDD == this.orbDeflatesWhen + 1)
                    {
                        this.motionX = 0.0D; this.motionY = 0.0D; this.motionZ = 0.0D;
                    }

                    this.motionX += extraX;
                    this.motionY += extraY;
                    this.motionZ += extraZ;
                }
            }
        }
    }


    @Override
    protected void dyingBurst(boolean fromDeath, int value) 
    {
//Get "self-exploding" step
        int i = this.getSelfeState();
//Increment fused timer by the step
        this.timeSinceIgnited += i * value;

//And make sure it's not negative
        if (this.timeSinceIgnited < 0) 
        {
            this.timeSinceIgnited = 0;
        }


//If already fully fused
        if (this.timeSinceIgnited >= this.getFuseState()) 
        {
//Don't go over fuse limit
            this.timeSinceIgnited = this.getFuseState();
//Run orb activity + death check
            this.selfExplode();
        } 
//If not fully fused scale it up
        else 
        {
            this.setSize(this.width + (0.8F * this.orbVoidMixin.getGrowthRate()), this.height + (0.32F * this.orbVoidMixin.getGrowthRate()));
        }
    }




    public boolean performOwnerValidation()
    {
        return this.validateOwner();
    }


    public boolean ownerValidConditions(Entity toValidate)
    {
        return (toValidate instanceof EntityLivingBase);
    }


//Validate owner and return if successful
    public boolean validateOwner()
    {

//If there is a owner UUID
        if(this.ownerUUID != null)
        {
//But no valid owner 
            if(this.owner == null)
            {
//Try to get owner from UUID
                Entity foundEntity  
                = ((WorldServer)world).getEntityFromUuid(this.ownerUUID);


//If owner found
//and owner conditions met
                if(foundEntity != null && this.ownerValidConditions(foundEntity))
                {
//Restore owner
                    this.owner = (EntityLiving) foundEntity;
//Check successful
                    return true;
                }
            }

//If there's both a owner and its UUID
            else
            {
//Check successful
                return true;
            }
        }


//If no UUID, check failed
        return false;

    }




//Visual disappearing logic
    @Override
    protected void selfExplode() 
    {
        this.setSelfeState(2);


        if (this.getSelfeState() == 2) 
        {
            if(!this.world.isRemote) { ++this.timerDDD; }
            if (this.timerDDD > this.orbDeflatesWhen) 
            {
                this.setSize(Math.max(0.1F, this.width - (0.8F * this.orbVoidMixin.getDeflateRate())), 
                             Math.max(0.1F, this.height - (0.32F * this.orbVoidMixin.getDeflateRate())));
                if (this.world.isRemote) 
                {
                    int par = this.getFuseState();
                    par += par / 2;

                    for(int i = 0; i <= par; ++i) 
                    {
                        this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D, this.posY + this.rand.nextDouble() * 2.0D * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width * 2.0D, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[]{0, 0, 0});
                    }
                }

//              this.playSound(SRPSounds.ORB_E, 1.0F, 1.0F);
                if(!this.world.isRemote) 
                { 
                    if(this.timerDDD == this.orbDeflatesWhen + 1 && !this.dontSoundWarning)
                    {
                        this.playSound(EternityModeSoundEvents.ENTITY_STAR_WINDUP, 8.0F, 1.0F);
                    }
                    if (this.timerDDD > orbDiesWhen) 
                    {
                        if(!this.dontVisualExplosion)
                        {
                            PacketUtil.sendPacketToNearbyPlayers(this.world, this.posX, this.posY, this.posZ, 999.0D, 
                                new ExplosionVisualPacket(1, this.posX, this.posY, this.posZ, 1.2F * this.orbVoidMixin.getGrowthRate() * this.getFuseState(), false));
                        }
                        if(!this.dontSoundExplosion)
                        {
                            this.playSound(EternityModeSoundEvents.ENTITY_SLAM_EXPLOSION, 8.0F, 1.0F);
                        }
                        this.releaseBlocks(); 
                        this.setDead();
                    }
                }
            }
        }
    }


    public void releaseBlocks()
    {
        int totalScatterBlocks = this.scatterBlockCount;


        if(!this.orbBlocks.isEmpty())
        {
//Aimed blocks
            if(this.owner != null)
            {
                EntityLivingBase ownerTarget = owner.getAttackTarget();
//If owner has target
                if(ownerTarget != null)
                {
//Make aim vectors around target
                    Vec3d targetVec = new Vec3d(ownerTarget.posX - this.posX, (ownerTarget.posY + 8.0D) - this.posY, ownerTarget.posZ - this.posZ);
                    ArrayList<Vec3d> aimVecs = DirectionalSpreadUtil.fibonacciDirectionalSpread(targetVec, this.aimedBlockCount, 0.2D * Math.PI);

//Get target vec length
                    double targetDist = targetVec.length();
//Get force factor
                    double extraForceFactor = 0.01D * this.blockForceGeneral;

                    for(Vec3d vec : aimVecs)
                    {
                        int randomBlockIndex = this.rand.nextInt(this.orbBlocks.size());

                        EntityBlockData blockData = this.orbBlocks.get(randomBlockIndex);

                        EntityThrownBlock block = new EntityThrownBlock(this.world, this.posX, this.posY, this.posZ, 
                            this.owner, blockData.basisState, 
                            true, true, true, blockData.thrownBlockDamage);
                        block.setOrigin(blockData.blockOrigin);
                        block.hasManualOrigin = true;
                        block.dontBreakInitialPos = true;

//Each block vec has a base amount,
//multiplies more based on target distance, then slightly randomizes
                        block.setMovement(vec.x * (1.0D + (targetDist * extraForceFactor)) * (1.0D + (this.rand.nextDouble() * 0.1D)), 
                            vec.y * (1.0D + (targetDist * extraForceFactor)) * (1.0D + (this.rand.nextDouble() * 0.1D)), 
                            vec.z * (1.0D + (targetDist * extraForceFactor)) * (1.0D + (this.rand.nextDouble() * 0.1D)), 
                            0.08D, false, 1.0D);

                        this.world.spawnEntity(block);
                    }
                }
                else
                {
                    totalScatterBlocks += this.aimedBlockCount * 2;
                }
            }
            else
            {
                totalScatterBlocks += this.aimedBlockCount * 2;
            }


//Scattered blocks
            for(int scatter = 0; scatter < totalScatterBlocks; scatter++)
            {
                int randomBlockIndex = this.rand.nextInt(this.orbBlocks.size());

                EntityBlockData blockData = this.orbBlocks.get(randomBlockIndex);

                EntityThrownBlock block = new EntityThrownBlock(this.world, this.posX, this.posY, this.posZ, 
                    this.owner, blockData.basisState, 
                    true, true, true, blockData.thrownBlockDamage);
                block.setOrigin(blockData.blockOrigin);
                block.hasManualOrigin = true;
                block.dontBreakInitialPos = true;

                double shootRadian = this.rand.nextDouble() * 2.0D * Math.PI;
                block.setMovement(this.rand.nextDouble() * Math.cos(shootRadian) * this.blockForceHorizontal, 
                    this.blockForceVertical * this.rand.nextDouble(), 
                    this.rand.nextDouble() * Math.sin(shootRadian) * this.blockForceHorizontal, 
                    0.08D, false, 1.0D);

                this.world.spawnEntity(block);
            }
        }
    }


//Clean out regular behavior
    protected void orbDoingCustom() 
    {
        if(this.ticksExisted % 20 == 0)
        {
            if(!this.dontSoundActive)
            {
                this.playSound(EternityModeSoundEvents.ENTITY_BLASTER_CHARGING, 6.0F, 1.0F);
            }
        }
    }
   

    @Override
    public void pullEntity(EntityLivingBase target) 
    {

    }




//Scale flash intensity appropriately to max fuse
    @SideOnly(Side.CLIENT)
    public float getSelfeFlashIntensity(float p_70831_1_) 
    {
        return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_ * 5.0F) 
            / (float) ((float) this.getFuseState() - (float) (2.0F * ((float) this.getFuseState() / 8.0F)));
    }




//New getters

    public int getTimeSinceIgnited()
    {
        return this.timeSinceIgnited;
    }

    public int getTimerDDD()
    {
        return this.timerDDD;
    }

}
