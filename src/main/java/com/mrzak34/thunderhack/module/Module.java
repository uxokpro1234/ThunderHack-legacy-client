package com.mrzak34.thunderhack.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.mrzak34.thunderhack.command.Command;
import com.mrzak34.thunderhack.event.events.PacketEvent;
import com.mrzak34.thunderhack.settings.KeybindSetting;
import com.mrzak34.thunderhack.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Module {

    public static Minecraft mc = Minecraft.getMinecraft();
    static Object Category;

    public String name;
    private KeybindSetting keyCode = new KeybindSetting(0);
    private Category category;
    public boolean toggled;
    public List<Setting> settings = new ArrayList<Setting>();
    public boolean showSettings;

    public static boolean fullNullCheck() {
        return Module.mc.player == null || Module.mc.world == null;
    }

    public Module(String name,int key , Category category, boolean showSettings) {
        super();
        this.name = name;
        keyCode.code = key;
        this.category = category;
        this.toggled = false;
        this.showSettings = showSettings;
        this.addSettings(keyCode);
    }

    public void addSettings(Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
        this.settings.sort(Comparator.comparingInt(s -> s == keyCode ? 1 : 0));
    }

    public int getKey(){
        return keyCode.code;
    }

    public void setKey(int key){
        keyCode.code = key;
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
        Command.sendSilentMessage(ChatFormatting.BOLD + "<"+getName()+">" + " " + ChatFormatting.GREEN + "enabled");


    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        Command.sendSilentMessage(ChatFormatting.BOLD + "<"+getName()+">" + " " + ChatFormatting.RED + "disabled");

    }



    public String getName() {
        return this.name;
    }

    public Category getCategory() {
        return this.category;
    }

    protected PacketEvent.Send onPacketSend() {
        return null;
    }
}
