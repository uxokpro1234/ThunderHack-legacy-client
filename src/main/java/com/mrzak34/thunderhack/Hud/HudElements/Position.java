package com.mrzak34.thunderhack.Hud.HudElements;

import com.mrzak34.thunderhack.Hud.Hud;
import com.mrzak34.thunderhack.Hud.HudMenu;

public class Position extends Hud {

    public static int xpos = 2;
    public static int ypos = 2;
    public static Hud position;

    public Position(){
        super("Position", HudMenu.HUD, false, false);
    }
}
