package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.event.events.PacketEvent;
import com.mrzak34.thunderhack.event.events.PlayerMoveEvent;
import com.mrzak34.thunderhack.event.events.UpdateWalkingPlayerEvent;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.RenderUtil;
import com.mrzak34.thunderhack.util.RotationUtil;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.util.Timer;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class NcpBypass extends Module {
    public NumberSetting packets = new NumberSetting("Packets",2,1,50,1);
    public NumberSetting speed  = new NumberSetting("Speed",2,1,100,1);
    public NumberSetting Upspeed  = new NumberSetting("UpSpeed",1,1,100,1);
    public NumberSetting glide  = new NumberSetting("Glide",0,1,100,1);
    public BooleanSetting nclip  = new BooleanSetting("NoClip",false);

    public Timer timer = new Timer();
    public NcpBypass(){
        super("NcpBypass",0, Category.MOVEMENT,false);
        this.addSettings(packets,speed,Upspeed,nclip,glide);
    }

    @Override
    public void onDisable()  {
        super.onDisable();
        mc.player.capabilities.isFlying = false;
        mc.player.noClip = false;
        System.out.println("Аким аКИМ, Гупа Вилпа попуск, пердак, Tokenlogged succes, ezezezezezez");
    }
    @SubscribeEvent
    public void onUpdateMove(PlayerMoveEvent event) {
            if (nclip.isEnabled()) {
                mc.player.noClip = true;

        }
    }

    @SubscribeEvent
    public void onUpdate(UpdateWalkingPlayerEvent event) {

            mc.player.capabilities.isFlying = true;
        mc.player.motionY = -(float)glide.getValue() / 1000;

        if (timer.passedMs(2550)) {
                timer.reset();
                return;
            }
            if (timer.passedMs(2500)) {
                return;
            }
            double[] dir = RotationUtil.directionSpeed(speed.getValue() / 10);
            mc.player.capabilities.setFlySpeed((float) Upspeed.getValue() / 100);
            mc.player.motionZ = dir[1];
            mc.player.motionX = dir[0];
            for (int va = 0; va < packets.getValue(); va++) {
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.prevPosX, mc.player.prevPosY + 0.05, mc.player.prevPosZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
            }
        }
    double realx;
    double realy;
    double realz;

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                if (timer.passedMs(2500)) {
                    return;
                }
                realx = ((SPacketPlayerPosLook) event.getPacket()).x;
                realz = ((SPacketPlayerPosLook) event.getPacket()).z;
                realy = ((SPacketPlayerPosLook) event.getPacket()).y;
                if (true) {
                    ((SPacketPlayerPosLook) event.getPacket()).y = mc.player.posY;
                    ((SPacketPlayerPosLook) event.getPacket()).x = mc.player.posX;
                    ((SPacketPlayerPosLook) event.getPacket()).z = mc.player.posZ;
                    ((SPacketPlayerPosLook) event.getPacket()).yaw = mc.player.rotationYaw;
                    ((SPacketPlayerPosLook) event.getPacket()).pitch = mc.player.rotationPitch;
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.prevPosX, mc.player.prevPosY + 0.05, mc.player.prevPosZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                }
            }
        }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
                if (realx != 0) {

                    double sx = mc.player.boundingBox.maxX - mc.player.boundingBox.minX;
                    double sy = mc.player.boundingBox.maxY - mc.player.boundingBox.minY;
                    double sz = mc.player.boundingBox.maxZ - mc.player.boundingBox.minZ;

                    RenderUtil.drawBoundingBox(new AxisAlignedBB(realx - sx / 2, realy, realz - sz / 2, realx + sx / 2, realy + sy, realz + sz / 2), new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).getRGB());
                }
            }
        }


