package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.EntityUtil;
import io.netty.util.internal.ConcurrentSet;
import com.mrzak34.thunderhack.event.events.PlayerMoveEvent;
import com.mrzak34.thunderhack.event.events.PacketEvent;
import com.mrzak34.thunderhack.event.events.PushEvent;
import com.mrzak34.thunderhack.event.events.UpdateWalkingPlayerEvent;
import com.mrzak34.thunderhack.util.Timer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketFly extends Module {
    private static PacketFly instance;
    private final Set <CPacketPlayer> packets = new ConcurrentSet();
    private final Map <Integer, IDtime> teleportmap = new ConcurrentHashMap<>();
    public BooleanSetting flight = new BooleanSetting("Flight", true);
    public NumberSetting flightMode = new NumberSetting("FMode", 1, 0, 1, 1);
    public BooleanSetting doAntiFactor = new BooleanSetting("Factorize", true);
    public NumberSetting antiFactor = new NumberSetting("AntiFactor", 25, 1, 30, 1);
    public NumberSetting extraFactor = new NumberSetting("ExtraFactor", 1.0, 0.1, 3.0, 0.1);
    public BooleanSetting strafeFactor = new BooleanSetting("StrafeFactor", true);
    public NumberSetting loops = new NumberSetting("Loops", 1, 1, 10, 1);
    public BooleanSetting clearTeleMap = new BooleanSetting("ClearMap", true);
    public NumberSetting mapTime = new NumberSetting("ClearTime", 30, 1, 500, 1);
    public BooleanSetting clearIDs = new BooleanSetting("ClearIDs", true);
    public BooleanSetting setYaw = new BooleanSetting("SetYaw", true);
    public BooleanSetting setID = new BooleanSetting("SetID", true);
    public BooleanSetting setMove = new BooleanSetting("SetMove", false);
    public BooleanSetting nocliperino = new BooleanSetting("NoClip", true);
    public BooleanSetting sendTeleport = new BooleanSetting("Teleport", true);
    public BooleanSetting resetID = new BooleanSetting("ResetID", true);
    public BooleanSetting setPos = new BooleanSetting("SetPos", false);
    public BooleanSetting invalidPacket = new BooleanSetting("InvalidPacket", true);
    private int flightCounter;
    private int teleportID;

    public PacketFly() {
        super("PacketFly",  0 , Category.MOVEMENT, false);
        this.addSettings(flight, flightMode,
                        doAntiFactor,antiFactor,
                        extraFactor,strafeFactor,loops,
                        clearTeleMap,mapTime,
                        clearIDs,setYaw,
                        setID,setMove,
                        nocliperino,sendTeleport,
                        resetID,setPos,
                        invalidPacket);
                        instance = this;
    }

    public static PacketFly getInstance() {
        if ( instance == null){
            instance = new PacketFly();
        }
        return instance;
    }



    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        this.teleportmap.entrySet().removeIf (idTime -> this.clearTeleMap.isEnabled() && idTime.getValue().getTimer().passedS(this.mapTime.getValue()));
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer (UpdateWalkingPlayerEvent event) {
        if ( event.getStage() == 1) {
            return;
        }
        PacketFly.mc.player.setVelocity (0.0 , 0.0 , 0.0);
        double speed;
        boolean checkCollisionBoxes = this.checkHitBoxes();
        speed = PacketFly.mc.player.movementInput.jump && (checkCollisionBoxes || !EntityUtil.isMoving()) ? (this.flight.isEnabled() && !checkCollisionBoxes ? (this.flightMode.getValue() == 0 ? (this.resetCounter (10) ? - 0.032 : 0.062) : (this.resetCounter (20) ? - 0.032 : 0.062)) : 0.062) : (PacketFly.mc.player.movementInput.sneak ? - 0.062 : (!checkCollisionBoxes ? (this.resetCounter(4) ? (this.flight.isEnabled() ? - 0.04 : 0.0): 0.0 ): 0.0 ));
        if ( this.doAntiFactor.isEnabled() && checkCollisionBoxes && EntityUtil.isMoving() && speed != 0.0){
            speed /= this.antiFactor.getValue() / 10;
        }
        double[] strafing = this.getMotion (this.strafeFactor.isEnabled() && checkCollisionBoxes ? 0.031 : 0.26);
        for (int i = 1; i < this.loops.getValue() + 1; ++ i) {
            PacketFly.mc.player.motionX = strafing[0] * (double) i * this.extraFactor.getValue();
            PacketFly.mc.player.motionY = speed * (double) i;
            PacketFly.mc.player.motionZ = strafing[1] * (double) i * this.extraFactor.getValue();
            this.sendPackets (PacketFly.mc.player.motionX , PacketFly.mc.player.motionY , PacketFly.mc.player.motionZ , this.sendTeleport.isEnabled ( ) );
        }
    }

    @SubscribeEvent
    public void onMove(PlayerMoveEvent event) {
        if ( this.setMove.isEnabled () && this.flightCounter != 0){
            event.setX(PacketFly.mc.player.motionX);
            event.setY(PacketFly.mc.player.motionY);
            event.setZ(PacketFly.mc.player.motionZ);
            if (this.nocliperino.isEnabled ( ) && this.checkHitBoxes()){
                PacketFly.mc.player.noClip = true;
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket () instanceof CPacketPlayer && ! this.packets.remove (event.getPacket ())) {
            event.setCanceled (true);
        }
    }

    @SubscribeEvent
    public void onPushOutOfBlocks(PushEvent event) {
        if (event.getStage () == 1){
            event.setCanceled (true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && ! PacketFly.fullNullCheck()){

            SPacketPlayerPosLook packet = event.getPacket();
            if (PacketFly.mc.player.isEntityAlive() && PacketFly.mc.world.isBlockLoaded (new BlockPos(PacketFly.mc.player.posX , PacketFly.mc.player.posY , PacketFly.mc.player.posZ) , false ) && ! ( PacketFly.mc.currentScreen instanceof GuiDownloadTerrain ) && this.clearIDs.isEnabled ( ) ) {
                this.teleportmap.remove (packet.getTeleportId());
            }
            if ( this.setYaw.isEnabled()) {
                packet.yaw = PacketFly.mc.player.rotationYaw;
                packet.pitch = PacketFly.mc.player.rotationPitch;
            }
            if ( this.setID.isEnabled()) {
                this.teleportID = packet.getTeleportId();
            }
        }
    }


    private boolean checkHitBoxes() {
        return ! PacketFly.mc.world.getCollisionBoxes(PacketFly.mc.player , PacketFly.mc.player.getEntityBoundingBox().expand(- 0.0625 , - 0.0625 , - 0.0625 ) ).isEmpty();
    }

    private boolean resetCounter(int counter) {
        if (++ this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }

    private double[] getMotion(double speed) {
        float moveForward = PacketFly.mc.player.movementInput.moveForward;
        float moveStrafe = PacketFly.mc.player.movementInput.moveStrafe;
        float rotationYaw = PacketFly.mc.player.prevRotationYaw + (PacketFly.mc.player.rotationYaw - PacketFly.mc.player.prevRotationYaw ) * mc.getRenderPartialTicks ( );
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? - 45 : 45);
            } else if (moveStrafe < 0.0f ) {
                rotationYaw += (float) (moveForward > 0.0f ? 45 : - 45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = - 1.0f;
            }
        }
        double posX = (double) moveForward * speed * - Math.sin (Math.toRadians (rotationYaw)) + (double) moveStrafe * speed * Math.cos (Math.toRadians (rotationYaw));
        double posZ = (double) moveForward * speed * Math.cos (Math.toRadians (rotationYaw)) - (double) moveStrafe * speed * - Math.sin (Math.toRadians (rotationYaw));
        return new double[]{posX , posZ};
    }

    private void sendPackets(double x , double y , double z , boolean teleport) {
        Vec3d vec = new Vec3d (x , y , z);
        Vec3d position = PacketFly.mc.player.getPositionVector ().add (vec);
        Vec3d outOfBoundsVec = outOfBoundsVec (vec , position);
        this.packetSender (new CPacketPlayer.Position (position.x , position.y , position.z , PacketFly.mc.player.onGround));
        if (this.invalidPacket.isEnabled()) {
            this.packetSender (new CPacketPlayer.Position (outOfBoundsVec.x , outOfBoundsVec.y , outOfBoundsVec.z , PacketFly.mc.player.onGround ) );
        }
        if (this.setPos.isEnabled()) {
            PacketFly.mc.player.setPosition (position.x , position.y , position.z);
        }
        this.teleportPacket(position , teleport);
    }

    private void teleportPacket(Vec3d pos , boolean shouldTeleport) {
        if (shouldTeleport) {
            PacketFly.mc.player.connection.sendPacket(new CPacketConfirmTeleport(++ this.teleportID));
            this.teleportmap.put(teleportID ,new IDtime (pos , new Timer()));
        }
    }

    private Vec3d outOfBoundsVec(Vec3d offset , Vec3d position) {
        return position.add (0.0 , 1337.0 , 0.0);
    }

    private void packetSender(CPacketPlayer packet) {
        this.packets.add(packet);
        PacketFly.mc.player.connection.sendPacket(packet);
    }

    private void clean() {
        this.teleportmap.clear();
        this.flightCounter = 0;
        if ( this.resetID.isEnabled()) {
            this.teleportID = 0;
        }
        this.packets.clear();
    }

    public static class IDtime {
        private final Vec3d pos;
        private final Timer timer;

        public IDtime(Vec3d pos , Timer timer) {
            this.pos = pos;
            this.timer = timer;
            this.timer.reset();
        }

        public Vec3d getPos() {
            return this.pos;
        }

        public Timer getTimer() {
            return this.timer;
        }
    }
}

