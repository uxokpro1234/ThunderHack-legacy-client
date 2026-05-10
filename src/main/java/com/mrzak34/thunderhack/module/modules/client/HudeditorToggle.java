package com.mrzak34.thunderhack.module.modules.client;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.ui.click.Clickgui;
import com.mrzak34.thunderhack.ui.click.Hudeditor;
import org.lwjgl.input.Keyboard;

public class HudeditorToggle extends Module  {

    public Hudeditor hudEditor = new Hudeditor();

    public HudeditorToggle(){
        super("HudEditor",0 , Category.CLIENT, false);
        this.setKey(Keyboard.KEY_NUMPAD4);
    }

    public void onEnable() {
        mc.displayGuiScreen(hudEditor);
    }
}
