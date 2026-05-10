package com.mrzak34.thunderhack.module.modules.combat;

import com.mrzak34.thunderhack.event.events.PacketEvent;
import com.mrzak34.thunderhack.event.events.PushEvent;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.modules.movement.IceSpeed;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity
        extends Module {

    public BooleanSetting knockBack = new BooleanSetting("KnockBack", true);
    public BooleanSetting noPush = new BooleanSetting("NoPush", true);
    public NumberSetting horizontal = new NumberSetting("Horizontal", 0,0,100,1);
    public NumberSetting vertical = new NumberSetting("Vertical", 0,0,100,1);
    public BooleanSetting explosions = new BooleanSetting("Explosions", true);
    public BooleanSetting bobbers = new BooleanSetting("Bobbers", true);
    public BooleanSetting blocks = new BooleanSetting("Blocks", true);
    public BooleanSetting water = new BooleanSetting("Water", true);
    public BooleanSetting ice = new BooleanSetting("Ice", true);

    public Velocity() {
        super("Velocity", 0, Category.MOVEMENT,  false);
        this.addSettings(knockBack,noPush,horizontal,vertical,explosions,bobbers,blocks,water,ice);
    }



    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {

            Blocks.ICE.slipperiness = 0.6f;
            Blocks.PACKED_ICE.slipperiness = 0.6f;
            Blocks.FROSTED_ICE.slipperiness = 0.6f;
    }

    @Override
    public void onDisable() {
            Blocks.ICE.slipperiness = 0.98f;
            Blocks.PACKED_ICE.slipperiness = 0.98f;
            Blocks.FROSTED_ICE.slipperiness = 0.98f;
        super.onDisable();
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (event.getStage() == 0 && Velocity.mc.player != null) {
            Entity entity;
            SPacketEntityStatus packet;
            SPacketEntityVelocity velocity;
            if (this.knockBack.isEnabled() && event.getPacket() instanceof SPacketEntityVelocity && (velocity = (SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.entityId) {
                if ((float)this.horizontal.getValue() == 0.0f && (float) this.vertical.getValue() == 0.0f) {
                    event.setCanceled(true);
                    return;
                }
                velocity.motionX = (int)((float)velocity.motionX * (float)this.horizontal.getValue());
                velocity.motionY = (int)((float)velocity.motionY * (float) this.vertical.getValue());
                velocity.motionZ = (int)((float)velocity.motionZ * (float)this.horizontal.getValue());
            }
            if (event.getPacket() instanceof SPacketEntityStatus && this.bobbers.isEnabled()&& (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 31 && (entity = packet.getEntity((World)Velocity.mc.world)) instanceof EntityFishHook) {
                EntityFishHook fishHook = (EntityFishHook)entity;
                if (fishHook.caughtEntity == Velocity.mc.player) {
                    event.setCanceled(true);
                }
            }
            if (this.explosions.isEnabled() && event.getPacket() instanceof SPacketExplosion) {
                velocity = (SPacketEntityVelocity) event.getPacket();
                velocity.motionX *= (float)this.horizontal.getValue();
                velocity.motionY *= (float) this.vertical.getValue();
                velocity.motionZ *= (float)this.horizontal.getValue();
            }
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 0 && this.noPush.isEnabled() && event.entity.equals((Object)Velocity.mc.player)) {
            if ((float) this.horizontal.getValue() == 0.0f && (float) this.vertical.getValue() == 0.0f) {
                event.setCanceled(true);
                return;
            }
            event.x = -event.x * (double)this.horizontal.getValue();
            event.y = -event.y * (double)this.vertical.getValue();
            event.z = -event.z * (double)this.horizontal.getValue();
        } else if (event.getStage() == 1 && this.blocks.isEnabled()) {
            event.setCanceled(true);
        } else if (event.getStage() == 2 && this.water.isEnabled() && Velocity.mc.player != null && Velocity.mc.player.equals((Object)event.entity)) {
            event.setCanceled(true);
        }
    }
}