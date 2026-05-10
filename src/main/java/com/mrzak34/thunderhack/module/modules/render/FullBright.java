package com.mrzak34.thunderhack.module.modules.render;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.module.modules.exploit.PhobosAWP;
import com.mrzak34.thunderhack.settings.NumberSetting;
import net.minecraft.client.Minecraft;

public class FullBright extends Module {
    private Minecraft mc = Minecraft.getMinecraft();
    public NumberSetting gamma = new NumberSetting("gamma",1, 0, 10, 1);

    public FullBright() {
        super("FullBright",0 , Category.RENDER, false);
        this.addSettings(gamma);
    }
    private static FullBright INSTANCE = new FullBright();
    public static FullBright getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FullBright();
        }
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }


    public void onEnable(){
        mc.gameSettings.gammaSetting = (int) gamma.getValue();
    }
}