package com.mrzak34.thunderhack.module.modules.misc;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Fly extends Module {
    private Minecraft mc = Minecraft.getMinecraft();
    public Fly() {
        super("Fly",0, Category.MISC, false);
    }

    @Override
    public void onEnable() {
        mc.player.motionY = 0.0001f;
        mc.player.capabilities.isFlying = true;


        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.player.capabilities.isFlying = false;

        super.onDisable();
    }
}
