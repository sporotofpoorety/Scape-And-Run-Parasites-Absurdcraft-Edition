package org.sporotofpoorety.srpabsurdcraft.util;


import net.minecraft.entity.Entity;


import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityParasiteBase;
import com.dhanantry.scapeandrunparasites.entity.ai.misc.EntityPStationary;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityCruxB;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityInhooM;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityInhooS;
import com.dhanantry.scapeandrunparasites.entity.monster.crude.EntityLesh;
import com.dhanantry.scapeandrunparasites.entity.monster.inborn.*;
import com.dhanantry.scapeandrunparasites.entity.monster.pure.preeminent.EntityFlam;




public class ParasiteSpawnUtils 
{

/*
    public static boolean isStandardParasite(Entity entity)
    {
//Can't be a nexus, deterrent, 
//inborn, succor, moving flesh, incomplete form
        if(entity instanceof EntityParasiteBase
        && !(entity instanceof EntityPStationary)
//Gnat, flying carrier, worm carrier, worker
        && !(entity instanceof EntityAta) && !(entity instanceof EntityButhol) && !(entity instanceof EntityGothol) && !(entity instanceof EntityKol)
//buglin, ???, rupter, mangler        
        && !(entity instanceof EntityLodo) && !(entity instanceof EntityMor) && !(entity instanceof EntityMudo) && !(entity instanceof EntityNuuh)
//Heavy carrier, lice       
        && !(entity instanceof EntityRathol) && !(entity instanceof EntityViin)
        && !(entity instanceof EntityFlam)
        && !(entity instanceof EntityLesh) && !(entity instanceof EntityInhooM) && !(entity instanceof EntityInhooS) && !(entity instanceof EntityCruxB))
        {
            return true;
        }       


        return false;
    }
*/


    public static final String[] victimSuffixes =
    {
        "bear", "cow", "horse", "pig", "sheep", "squid", "wolf"
    };


    public static boolean isCothVictim(String parasiteName)
    {

//Check for incomplete forms
        if(parasiteName.startsWith("incomplete") || parasiteName.endsWith("incomplete"))
        { return true; }


//Check if right tier
        if(parasiteName.startsWith("sim_") || parasiteName.startsWith("fer_") || parasiteName.startsWith("mar_"))
        {

//Specific enderman check to not screw with mutant sim enderman
            if(parasiteName.equals("sim_enderman") || parasiteName.equals("fer_enderman") || parasiteName.equals("mar_enderman"))
            { return true; }


//Check if right suffix
            for(String suffix : victimSuffixes)
            {
                if(parasiteName.endsWith(suffix))
                {
                    return true;
                }
            }
        }


        return false;
    }

}
