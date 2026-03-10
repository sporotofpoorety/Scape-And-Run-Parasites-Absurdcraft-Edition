package org.sporotofpoorety.srpabsurdcraft.util;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigSystems;



public class SRPOverrideConfigSystems
{
    public static void SRPOverrideConfigSystemsApply()
    {


        SRPConfigSystems.COTHItemPrevent = new String[]{"minecraft:golden_apple;1.0;99999999", "minecraft:golden_carrot;1.0;99999999"};
        SRPConfigSystems.COTHImmuneList = new String[]{"minecraft:chicken", "minecraft:cow", "minecraft:donkey", "minecraft:horse", "minecraft:llama", "minecraft:mooshroom", "minecraft:mule", "minecraft:ocelot", "minecraft:parrot", "minecraft:pig", "minecraft:polar_bear", "minecraft:sheep", "minecraft:squid", "minecraft:wolf", "oe:baby_squid", "oe:baby_glow_squid", "oe:clam", "oe:cod", "oe:crab", "oe:dolphin", "oe:glow_squid", "oe:lobster", "oe:pufferfish", "oe:salmon", "oe:tropical_fish", "oe:turtle", "primitivemobs:chameleon", "primitivemobs:dodo", "primitivemobs:filch_lizard", "primitivemobs:grovesprite"};
        SRPConfigSystems.COTHImmuneListWhite = true;

    
        SRPConfigSystems.generationUse = false;


        SRPConfigSystems.useScent = false;
        SRPConfigSystems.scentDeathSpawning = 0.0F;


        SRPConfigSystems.evolutionSleepDenied = 0;
        SRPConfigSystems.evolutionTotalKill = 11;
        SRPConfigSystems.evolutionNoParasiteHealing = 11;
        SRPConfigSystems.evolutionNoParasiteSpawnDenied = 11;
        SRPConfigSystems.evolutionParasiteStatIncrease = 11;
        SRPConfigSystems.evolutionPArasitesWithoutXP = 11;


        SRPConfigSystems.deveMobChance = 0.0D;

        SRPConfigSystems.deveDisloUse = 0;
        SRPConfigSystems.deveMergeUse = 0;
        SRPConfigSystems.deveOnemindUse = 0;
        SRPConfigSystems.deveScentUse = 0;
        SRPConfigSystems.deveOriginlessUse = 0;
        SRPConfigSystems.deveColoniesUse = 0;
        SRPConfigSystems.deveNodesUse = 0;
        SRPConfigSystems.deveHivesUse = 0;
        SRPConfigSystems.deveNestsUse = 0;
        SRPConfigSystems.deveAlwaysVariantUse = 0;


        SRPConfigSystems.fearActive = false;
        SRPConfigSystems.fearFallDamage = 0.0F;
        SRPConfigSystems.fearAirDamage = 0.0F;
        SRPConfigSystems.fearBlockChance = 0.0F;
        SRPConfigSystems.fearItemChance = 0.0F;
        SRPConfigSystems.fearInvChance = 0.0F;
        SRPConfigSystems.cothActive = true;
        SRPConfigSystems.cothPlayer = false;
        SRPConfigSystems.cothAura = 3;
        SRPConfigSystems.cothConvert = 1.0F;
        SRPConfigSystems.cothInfected = 1.0F;
        SRPConfigSystems.cothHijacked = 1.0F;
        SRPConfigSystems.cothFeral = 1.0F;
        SRPConfigSystems.cothCrude = 1.0F;
        SRPConfigSystems.cothPrimitive = 1.0F;
        SRPConfigSystems.cothAdapted = 1.0F;
        SRPConfigSystems.cothPure = 1.0F;
        SRPConfigSystems.bleedingDamage = 0.0F;
        SRPConfigSystems.bleedingDamageCap = 0.1F;
        SRPConfigSystems.corrNot = 0.99D;
        SRPConfigSystems.viralEnable = false;
        SRPConfigSystems.viralAmount = 0.00001F;
        SRPConfigSystems.rageEnable = false;
        SRPConfigSystems.rageDamage = 0.0D;
        SRPConfigSystems.rageSpeed = 0.0D;
        SRPConfigSystems.needlerDamage = 0.0F;
        SRPConfigSystems.needlerTerminal = 99;
        SRPConfigSystems.needlerImmuneListWhite = true;
        SRPConfigSystems.needlerMaxDamPlayer = 0.0F;
        SRPConfigSystems.needlerMaxDamMonster = 0.0F;
        SRPConfigSystems.adapsChance = 0.0D;
        SRPConfigSystems.parateMuch = 0.0D;
        SRPConfigSystems.hijackHealth = 0.1F;
        SRPConfigSystems.pivotDamageRHost = 0.0F;
        SRPConfigSystems.pivotDamageRNotHost = 1.0F;
        SRPConfigSystems.parasiteKillingReduction = 0.0F;
        SRPConfigSystems.muscleoutDamageOut = 0.0F;


        SRPConfigSystems.defaultEvoPhase = 4;
        SRPConfigSystems.defaultEvoPoints = 31000;
        SRPConfigSystems.evolutionDimStart = new String[]{"-1;4;31000", "0;0;0", "1;8;510000000"};


        SRPConfigSystems.cropGrowStunnedNine = 0.8D;
        SRPConfigSystems.cropGrowStunnedTen = 0.8D;


        SRPConfigSystems.mobSpawningCOTHChanceFour = 0.4D;
        SRPConfigSystems.mobSpawningCOTHChanceFive = 0.5D;
        SRPConfigSystems.mobSpawningCOTHChanceSix = 0.6D;
        SRPConfigSystems.mobSpawningCOTHChanceSeven = 0.7D; 
        SRPConfigSystems.mobSpawningCOTHChanceEight = 0.8D;
    }
}
