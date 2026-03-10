package org.sporotofpoorety.srpabsurdcraft.util;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfig;



public class SRPOverrideConfig 
{
    public static void SRPOverrideConfigApply()
    {
        SRPConfig.infectedCap = 1;
        SRPConfig.assimaraCap = 1;
        SRPConfig.feralCap = 1;
        SRPConfig.hijackedCap = 1;
        SRPConfig.primitiveCap = 1;
        SRPConfig.adaptedCap = 1;
        SRPConfig.pureCap = 1;
        SRPConfig.preeminentCap = 1;
        SRPConfig.derivedCap = 1;
        SRPConfig.ancientCap = 1;
        SRPConfig.nexussiCap = 1;
        SRPConfig.nexussiiCap = 1;
        SRPConfig.nexussiiiCap = 1;
        SRPConfig.nexussivCap = 1;
        SRPConfig.turretCap = 1;


        SRPConfig.blackListedDimensions = new int[]{};
        SRPConfig.entitiesWillAttack = new String[]{"minecraft:enderman", "minecraft:villager_golem"};


        SRPConfig.blockDropChance = 0.0D;
        SRPConfig.doTileDrops = false;
        SRPConfig.parasiteGriefing = new String[]{"srparasites:sim_dragone;99999999;600;4", "srparasites:hi_golem;3;80;2", "srparasites:mar_cow;3;80;1", "srparasites:mar_enderman;3;80;1", "srparasites:mar_bear;3;80;1", "srparasites:pri_longarms;3;120;1", "srparasites:pri_reeker;3;120;1", "srparasites:pri_summoner;3;120;1", "srparasites:pri_manducater;3;120;1", "srparasites:pri_yelloweye;99999999;600;1", "srparasites:pri_arachnida;3;120;1", "srparasites:pri_bolster;3;120;1", "srparasites:pri_vermin;99999999;600;1", "srparasites:pri_devourer;3;200;2", "srparasites:beckon_si;3;120;1", "srparasites:dispatcher_si;3;120;1", "srparasites:ada_longarms;3;80;1", "srparasites:ada_reeker;3;80;1", "srparasites:ada_summoner;3;80;1", "srparasites:ada_manducater;3;80;2", "srparasites:ada_yelloweye;99999999;400;2", "srparasites:ada_arachnida;3;80;2", "srparasites:ada_bolster;3.5;40;2", "srparasites:ada_devourer;3.5;120;3", "srparasites:beckon_sii;5.5;40;2", "srparasites:dispatcher_sii;5.5;40;2", "srparasites:warden;5;40;2", "srparasites:marauder;5;40;3", "srparasites:vigilante;5;40;2", "srparasites:overseer;99999999;200;2", "srparasites:bomber_light;99999999;200;2", "srparasites:grunt;3;60;1", "srparasites:monarch;5;40;4", "srparasites:beckon_siii;7;40;4", "srparasites:dispatcher_siii;7;40;4", "srparasites:wraith;99999999;600;5", "srparasites:bogle;99999999;600;5", "srparasites:haunter;15;120;5", "srparasites:carrier_colony;15;120;5", "srparasites:succor;99999999;600;2", "srparasites:bomber_heavy;99999999;600;5", "srparasites:beckon_siv;18;40;5", "srparasites:dispatcher_siv;18;40;5", "srparasites:kyphosis;7;40;3", "srparasites:sentry;7;40;3", "srparasites:anc_dreadnaut;99999999;400;4", "srparasites:anc_overlord;9;60;4", "srparasites:draconite;99999999;800;6", "srparasites:kirin;99999999;600;3", "srparasites:crux;4;60;2"};




        SRPConfig.globalHealthMultiplier = 1.0F;
        SRPConfig.globalArmorMultiplier = 0.01F;
        SRPConfig.globalDamageMultiplier = 0.3F;
        SRPConfig.globalKDResistanceMultiplier = 1.0F;




        SRPConfig.tendrilHealth = 0.05D;
        SRPConfig.derivedHackHealing = 0.01F;
        SRPConfig.derivedHackingEffects = new String[]{"140;minecraft:night_vision;1"};




        SRPConfig.feralRegen = 0.12F;
        SRPConfig.primitiveRegen = 0.16F;
        SRPConfig.adaptedRegen = 0.28F;
        SRPConfig.ancientRegen = 1.2F;
        SRPConfig.pureRegen = 0.56F;
        SRPConfig.preeminentRegen = 0.8F;
        SRPConfig.derivedRegen = 1.0F;
        SRPConfig.turretRegen = 0.4F;


        SRPConfig.mobattackingBlackList = new String[]{"minecraft:chicken", "minecraft:cow", "minecraft:donkey", "minecraft:enderman", "minecraft:horse", "minecraft:llama", "minecraft:mooshroom", "minecraft:mule", "minecraft:ocelot", "minecraft:parrot", "minecraft:pig", "minecraft:polar_bear", "minecraft:sheep", "minecraft:squid", "minecraft:wolf", "oe:baby_squid", "oe:baby_glow_squid", "oe:clam", "oe:cod", "oe:crab", "oe:dolphin", "oe:glow_squid", "oe:lobster", "oe:pufferfish", "oe:salmon", "oe:tropical_fish", "oe:turtle", "primitivemobs:chameleon", "primitivemobs:dodo", "primitivemobs:filch_lizard", "primitivemobs:grovesprite"};
        SRPConfig.mobattackingBlackListWhite = true;
        SRPConfig.mobAttackingFull = true;
    }
}
