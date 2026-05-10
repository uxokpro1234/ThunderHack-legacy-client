package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import net.minecraft.client.Minecraft;

public class SpoofFly extends Module {
    private Minecraft mc = Minecraft.getMinecraft();


    public SpoofFly() {

        super("AirJump",0, Category.MOVEMENT, false);}


    @Override
    public void onEnable() {
        mc.player.jump();
            toggle();
    }
}
