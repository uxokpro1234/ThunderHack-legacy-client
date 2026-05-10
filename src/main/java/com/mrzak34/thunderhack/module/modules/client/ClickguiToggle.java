package com.mrzak34.thunderhack.module.modules.client;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.ui.click.Clickgui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class ClickguiToggle extends Module {
    public static Minecraft mc = Minecraft.getMinecraft();

    public Clickgui clickGUI = new Clickgui();

    public static ScaledResolution sr = new ScaledResolution(mc);

    public static NumberSetting red = new NumberSetting("red",0, 0, 255, 1);
    public static NumberSetting green = new NumberSetting("green",255, 0, 255, 1);
    public static NumberSetting blue = new NumberSetting("blue",175, 0, 255, 1);
    public static BooleanSetting rainbow = new BooleanSetting("rainbow", false);
    public static NumberSetting rainbowdelay = new NumberSetting("rainbow delay",20, 1, 100, 1);



    public ClickguiToggle() {
        super("ClickGUI",0 ,  Category.CLIENT,false);
        this.setKey(Keyboard.KEY_RSHIFT);
        this.addSettings(red, green, blue, rainbow, rainbowdelay);
    }

    public void onEnable() {
        mc.displayGuiScreen(clickGUI);
    }

}
