package com.mrzak34.thunderhack.Hud;

import com.mrzak34.thunderhack.Hud.HudElements.Position;
import com.mrzak34.thunderhack.Main;

import java.util.ArrayList;
import java.util.List;

public class HudManager {

    public static ArrayList<Hud>huds;

    public HudManager() {
        (huds = new ArrayList<Hud>()).clear();
        this.huds.add(new Position());
    }

    public Hud getHud (String name) {

        for(Hud h : this.huds) {

            if(h.getName().equalsIgnoreCase(name)) {
                return h;
            }
        }
        return null;

    }
    public ArrayList<Hud> getHudList(){

        return this.huds;
    }

    public static List<Hud> getModulesByCategory(HudMenu hudmenu) {
        List<Hud> huds = new ArrayList<Hud>();

        for(Hud h : Main.hudManager.huds) {
            if(h.getHud() == hudmenu)
                huds.add(h);
        }
        return huds;
    }
}
