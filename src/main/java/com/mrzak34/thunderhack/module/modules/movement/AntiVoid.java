package com.mrzak34.thunderhack.module.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AntiVoid extends Module {

    public AntiVoid() {
        super("AntiVoid",0, Category.MOVEMENT, false);
    }

//advdwqbvdw
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (fullNullCheck()) {
            return;
        }

                RayTraceResult trace = mc.world.rayTraceBlocks(mc.player.getPositionVector(), new Vec3d(mc.player.posX, 0.0, mc.player.posZ), false, false, false);
                if (!mc.player.noClip && mc.player.posY <= 0.0) {
                    if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                        return;
                    }
                    mc.player.setVelocity(0.0, 0.0, 0.0);
                    if (mc.player.getRidingEntity() != null) {
                        mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
                    }
                }

    }

}


