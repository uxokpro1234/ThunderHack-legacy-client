package com.mrzak34.thunderhack.Hud;

import com.mrzak34.thunderhack.event.events.PacketEvent;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hud {

    public static Minecraft mc = Minecraft.getMinecraft();
    public int x;
    public int y;
    public String name;
    public boolean toggled;
    public List<Setting> settings = new ArrayList<Setting>();
    public boolean showSettings;
    private HudMenu hudmenu;
    public static boolean mouseClicked;

    public static boolean fullNullCheck() {
        return Module.mc.player == null || Module.mc.world == null;
    }

    public Hud(String name,HudMenu hudmenu,boolean mouseClicked, boolean showSettings) {
        super();
        this.name = name;
        this.toggled = false;
        this.showSettings = showSettings;
        this.hudmenu = hudmenu;
        this.mouseClicked = mouseClicked;
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;

        if(this.toggled) {
            this.onEnable();
        }else {
            this.onDisable();
        }
    }

    public void toggle() {
        this.toggled = !this.toggled;

        if(this.toggled) {
            this.onEnable();
        }else {
            this.onDisable();
        }
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public String getName() {
        return this.name;
    }

    public HudMenu getHud() {
        return this.hudmenu;
    }

    protected PacketEvent.Send onPacketSend() {
        return null;
    }
}
