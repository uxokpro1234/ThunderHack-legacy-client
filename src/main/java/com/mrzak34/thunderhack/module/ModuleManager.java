package com.mrzak34.thunderhack.module;

import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.module.modules.combat.*;
import com.mrzak34.thunderhack.module.modules.combat.NoSlowDown;
import com.mrzak34.thunderhack.module.modules.exploit.*;
import com.mrzak34.thunderhack.module.modules.movement.*;
import com.mrzak34.thunderhack.module.modules.misc.FakePlayer;
import com.mrzak34.thunderhack.module.modules.misc.Fly;
import com.mrzak34.thunderhack.module.modules.misc.NoBob;
import com.mrzak34.thunderhack.module.modules.render.*;
import com.mrzak34.thunderhack.module.modules.client.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public static ArrayList<Module>modules;
    public ModuleManager() {
        (modules = new ArrayList<Module>()).clear();
        //combat
        this.modules.add(new Velocity());
        this.modules.add(new KillAura());
        this.modules.add(new RuberBand());
        this.modules.add(new Criticals());
        this.modules.add(new AutoAim());
        this.modules.add(new AutoTotem());
        this.modules.add(new Burrow());
        this.modules.add(new NoSlowDown());
        this.modules.add(new PacketMine());
        //exploit
        this.modules.add(new PhobosAWP());
        this.modules.add(new TrueDurability());
        this.modules.add(new SpeedMine());
        this.modules.add(new NoFall());
        this.modules.add(new PortalGodMode());
        this.modules.add(new AntiHunger());
        this.modules.add(new EchestBypass());
        this.modules.add(new PacketCanceller());
        this.modules.add(new BowBomb());
        //misc
        this.modules.add(new FakePlayer());
        this.modules.add(new Fly());
        this.modules.add(new NoBob());
        this.modules.add(new Blink());
        //movement
        this.modules.add(new StairSpeed());
        this.modules.add(new AntiVoid());
        this.modules.add(new IceSpeed());
        this.modules.add(new SpoofFly());
        this.modules.add(new ReverseStep());
        this.modules.add(new OnGround());
        this.modules.add(new Static());
        this.modules.add(new NcpBypass());
        this.modules.add(new FastSwim());
        this.modules.add(new Speed());
        this.modules.add(new PacketFly());
        this.modules.add(new BoatFly());
        this.modules.add(new Sprint());
        //render
        this.modules.add(new FullBright());
        this.modules.add(new ItemViewModel());
        this.modules.add(new BlockHighlight());
        this.modules.add(new PortalESP());
        this.modules.add(new Nametags());
        this.modules.add(new PopChams());
        this.modules.add(new HoleESP());
        //client
        this.modules.add(Main.INSTANCE.clickGUI = new ClickguiToggle());
        this.modules.add(Main.INSTANCE.hudEditor = new HudeditorToggle());

        this.modules.add(new Image());

    }

    public Module getModule (String name) {

        for(Module m : this.modules) {
            if(m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
    public ArrayList<Module> getModuleList(){

        return this.modules;
    }

    public static List<Module> getModulesByCategory(Category c) {
        List<Module> module = new ArrayList<Module>();

        for(Module m : Main.moduleManager.modules) {
            if(m.getCategory() == c)
                modules.add(m);
        }
        return modules;
    }
}