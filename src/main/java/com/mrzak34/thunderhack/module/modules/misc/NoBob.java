package com.mrzak34.thunderhack.module.modules.misc;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import net.minecraft.client.Minecraft;

public class NoBob extends Module {
    private Minecraft mc = Minecraft.getMinecraft();


    public NoBob() {

        super("NoBob",0, Category.MISC, false);
    }

    public boolean oldvalue = false;


    @Override
    public void onEnable() {
        if( mc.gameSettings != null )
            mc.gameSettings.viewBobbing = false;

    }

    @Override
    public void onDisable() {
        mc.gameSettings.viewBobbing = oldvalue;

    }
}

