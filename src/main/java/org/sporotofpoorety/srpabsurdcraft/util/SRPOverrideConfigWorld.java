package org.sporotofpoorety.srpabsurdcraft.util;

import com.dhanantry.scapeandrunparasites.util.config.SRPConfigWorld;



public class SRPOverrideConfigWorld
{
    public static void SRPOverrideConfigWorldApply()
    {
        SRPConfigWorld.escapeEnabled = false;

        SRPConfigWorld.originActivated = false;
        SRPConfigWorld.originCap = 1;
        SRPConfigWorld.originRadius = 1;
        SRPConfigWorld.originHealth = 1;
        SRPConfigWorld.originRadiusCap = 2;
        SRPConfigWorld.originHealthCap = 2;
        SRPConfigWorld.originDailySize = 0.0D;
        SRPConfigWorld.originDailyHealth = 0.0D;
        SRPConfigWorld.originMinimumDistance = 1;
        SRPConfigWorld.originDailyEPPoints = 0.0D;
        SRPConfigWorld.originDailyEPPointsOutBreak = 0.0D;
        SRPConfigWorld.originCreatingRand = 9999999;
        SRPConfigWorld.originCreatingRandZero = 9999999;
        SRPConfigWorld.originCreatingDistanceMin = 9999999;
        SRPConfigWorld.originCreatingDistanceMax = 99999999;
        SRPConfigWorld.originSpotted = 9999999;
        SRPConfigWorld.originTriggerCOTH = 0.05D;
        SRPConfigWorld.originTriggerCOTHSpawn = 0.15D;
        SRPConfigWorld.originTriggerKill = 0.05D;
        SRPConfigWorld.originCOTHMultiplier = 1.0D;
        SRPConfigWorld.originParasiteDeath = 1.0D;
        SRPConfigWorld.originKillMultiplier = 1.0D;

        SRPConfigWorld.meteorTick = 9999999;
        SRPConfigWorld.meteorChance = 0.0D;
    }

}
