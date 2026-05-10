package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {
    private static Minecraft mc = Minecraft.getMinecraft();
    public ModeSetting mode = new ModeSetting("Mode", "LEGIT", "LEGIT", "FUCK");


    public Sprint() {
        super("Sprint",0, Category.MOVEMENT, false);
        this.addSettings(mode);

        

    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        switch(this.mode.getMode()){

            case "LEGIT":{
                if (!Sprint.mc.gameSettings.keyBindForward.isKeyDown() || Sprint.mc.player.isSneaking() || Sprint.mc.player.isHandActive() || Sprint.mc.player.collidedHorizontally || (float)Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f || Sprint.mc.currentScreen != null) break;
                Sprint.mc.player.setSprinting(true);}

            case "FUCK":{
                mc.player.setSprinting(true);}
            }

        }


    @Override
    public void onDisable() {
        if (!Sprint.fullNullCheck()) {
        mc.player.setSprinting(false);
    }
        super.onDisable();}
}

