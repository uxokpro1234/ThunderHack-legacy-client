package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Static
        extends Module {
    public ModeSetting mode  = new ModeSetting("Mode","Static","Static","Roof","NoVoid");
    public BooleanSetting disabler  = new BooleanSetting("Disable",false);
    public BooleanSetting ySpeed  = new BooleanSetting("YSpeed",false);
    public NumberSetting speed  = new NumberSetting("Speed",1f,1f,100f,1f);
    public NumberSetting height  = new NumberSetting("Height",3f,1f,256f,1f);


    public Static() {
        super("Static", 0, Category.MOVEMENT, false);
        this.addSettings(mode,disabler,ySpeed,speed,height);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Static.fullNullCheck()) {
            return;
        }
        switch (this.mode.getMode()) {
            case "Static": {
                Static.mc.player.capabilities.isFlying = false;
                Static.mc.player.motionX = 0.0;
                Static.mc.player.motionY = 0.0;
                Static.mc.player.motionZ = 0.0;
                if (!this.ySpeed.isEnabled()) break;
                Static.mc.player.jumpMovementFactor = (float)this.speed.getValue() / 10;
                if (Static.mc.gameSettings.keyBindJump.isKeyDown()) {
                    Static.mc.player.motionY += (double)this.speed.getValue() / 10;
                }
                if (!Static.mc.gameSettings.keyBindSneak.isKeyDown()) break;
                Static.mc.player.motionY -= (double)this.speed.getValue() / 10;
                break;
            }
            case "Roof": {
                Static.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Static.mc.player.posX, 10000.0, Static.mc.player.posZ, Static.mc.player.onGround));
                if (!this.disabler.isEnabled()) break;
                toggle();
                break;
            }
            case "NoVoid": {
                if (Static.mc.player.noClip || !(Static.mc.player.posY <= (double)this.height.getValue())) break;
                RayTraceResult trace = Static.mc.world.rayTraceBlocks(Static.mc.player.getPositionVector(), new Vec3d(Static.mc.player.posX, 0.0, Static.mc.player.posZ), false, false, false);
                if (trace != null && trace.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return;
                }

                Static.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (Static.mc.player.getRidingEntity() == null) break;
                Static.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
            }
        }
    }

}
