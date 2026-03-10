package org.sporotofpoorety.srpabsurdcraft.core;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;



@net.minecraftforge.fml.common.Optional.Interface(modid = "mixinbooter", iface = "zone.rong.mixinbooter.ILateMixinLoader")
public class SRPAbsurdcraftLateMixins implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList("mixins.late.srpabsurdcraft.json");
    }
}
