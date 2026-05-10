package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.module.modules.exploit.PhobosAWP;
import com.mrzak34.thunderhack.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;


public class IceSpeed extends Module {
    private static Minecraft mc = Minecraft.getMinecraft();
    public NumberSetting speed = new NumberSetting("Speed",4f, 2f, 15f, 1f);

    public IceSpeed() {
        super("IceSpeed",0, Category.MOVEMENT, false);
        this.addSettings(speed);

    }
    private static IceSpeed INSTANCE = new IceSpeed();
    public static IceSpeed getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IceSpeed();
        }
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        Blocks.ICE.slipperiness = (float) speed.getValue() / 10;
        Blocks.PACKED_ICE.slipperiness = (float) speed.getValue() / 10;
        Blocks.FROSTED_ICE.slipperiness = (float) speed.getValue() / 10;
    }
    @Override
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}

