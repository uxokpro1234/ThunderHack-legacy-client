package com.mrzak34.thunderhack.module.modules.movement;


import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StairSpeed
        extends Module {
    public StairSpeed() {
        super("StairSpeed", 0, Category.MOVEMENT, false);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (StairSpeed.mc.player.onGround && StairSpeed.mc.player.posY - Math.floor(StairSpeed.mc.player.posY) > 0.0 && StairSpeed.mc.player.moveForward != 0.0f) {
            StairSpeed.mc.player.jump();
        }
    }
}

