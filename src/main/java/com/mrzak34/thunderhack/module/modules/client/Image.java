package com.mrzak34.thunderhack.module.modules.client;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;

public class Image extends Module {

    public static ModeSetting mode = new ModeSetting("mode", "anime", "anime", "logo", "anime2", "none");
    public static NumberSetting imposX = new NumberSetting("imposX",0, 0, 1024, 1);
    public static NumberSetting imposY = new NumberSetting("imposY",0, 0, 1024, 1);
    public static NumberSetting Xscale = new NumberSetting("Xscale",100, 0, 1024, 1);
    public static NumberSetting Yscale = new NumberSetting("Yscale",100, 0, 1024, 1);
    public static NumberSetting fadetime = new NumberSetting("fadetime",0, 0, 1024, 1);

    public Image() {
        super("Image",0 , Category.CLIENT, false);
        this.addSettings(mode, imposX, imposY, Xscale, Yscale, fadetime);
    }
    public static boolean toggled = false;
    public boolean isToggled(){
        toggled = true;
        return false;
    }
}
