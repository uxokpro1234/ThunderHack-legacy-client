package com.mrzak34.thunderhack.module.modules.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import org.lwjgl.input.Keyboard;

public class RuberBand extends Module {

    public RuberBand() {
        super("Ruberband",0, Category.MOVEMENT, false);
        this.setKey(Keyboard.KEY_B);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        mc.player.setVelocity(0.0, 0.0, 0.0);
    }
}