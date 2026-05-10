package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.event.events.PlayerMoveEvent;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.NumberSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FastSwim
        extends Module {
    public NumberSetting waterHorizontal = new NumberSetting("WHorizontal",3,1,20,1);
    public NumberSetting waterVertical = new NumberSetting("WVertical",3,1,20,1);
    public NumberSetting lavaHorizontal = new NumberSetting("LHorizontal",3,1,20,1);
    public NumberSetting lavaVertical = new NumberSetting("LVertical",2,1,20,1);

    public FastSwim() {
        super("FastSwim", 0, Category.MOVEMENT, false);
        this.addSettings(waterHorizontal,waterVertical,lavaHorizontal,lavaVertical);
    }

    @SubscribeEvent
    public void onMove(PlayerMoveEvent event) {
        if (FastSwim.mc.player.isInLava() && !FastSwim.mc.player.onGround) {
            event.setX(event.getX() * this.lavaHorizontal.getValue());
            event.setZ(event.getZ() * this.lavaHorizontal.getValue());
            event.setY(event.getY() * this.lavaVertical.getValue());
        } else if (FastSwim.mc.player.isInWater() && !FastSwim.mc.player.onGround) {
            event.setX(event.getX() * this.waterHorizontal.getValue());
            event.setZ(event.getZ() * this.waterHorizontal.getValue());
            event.setY(event.getY() * this.waterVertical.getValue());
        }
    }
}
