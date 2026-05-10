package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.event.events.PlayerMoveEvent;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.util.BlockUtil;
import com.mrzak34.thunderhack.util.EntityUtil;
import com.mrzak34.thunderhack.util.MathUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovementInput;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Speed extends Module {
    private static Speed INSTANCE;
    private double highChainVal;
    private double lowChainVal;
    private boolean oneTime;
    public double startY;
    public boolean antiShake;
    private double bounceHeight;
    private float move;
    public static ModeSetting mode = new ModeSetting("Mode","INSTANT","INSTANT", "ONGROUND", "ACCEL", "BOOST");
    public static BooleanSetting strafeJump = new BooleanSetting("Jump",true);
    public static BooleanSetting noShake = new BooleanSetting("NoShake",true);
    public static BooleanSetting useTimer = new BooleanSetting("UseTimer",true);

    public Speed() {
        super("Speed", 0, Category.MOVEMENT, false);
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.oneTime = false;
        this.startY = 0.0;
        this.antiShake = false;
        this.bounceHeight = 0.4;
        this.move = 0.26f;

        this.setInstance();
        this.addSettings(mode,strafeJump,noShake,useTimer);
    }

    private void setInstance() {
        Speed.INSTANCE = this;
    }

    public static Speed getInstance() {
        if (Speed.INSTANCE == null) {
            Speed.INSTANCE = new Speed();
        }
        return Speed.INSTANCE;
    }



    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {

        if (Speed.mc.player.isSneaking() || Speed.mc.player.isInWater() || Speed.mc.player.isInLava()) {
            return;
        }
        switch (this.mode.getMode()) {
            case "BOOST": {
                this.doBoost();
                break;
            }
            case "ACCEL": {
                this.doAccel();
                break;
            }
            case "ONGROUND": {
                this.doOnground();
                break;
            }
        }
    }

    private void doBoost() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.player.onGround) {
            this.startY = Speed.mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.player) && !Speed.mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.isEnabled() && Speed.mc.player.getRidingEntity() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.player.posY >= this.startY + this.bounceHeight) {
                Speed.mc.player.motionY = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.15f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.2f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.225f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.25f;
                }
                if (this.lowChainVal >= 7.0) {
                    this.move = 0.27895f;
                }
                if (this.useTimer.isEnabled()) {
                    Main.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.player.posY == this.startY) {
                Speed.mc.player.motionY = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.325f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.4f;
                }
                if (this.highChainVal >= 6.0) {
                    this.move = 0.43395f;
                }
                if (this.useTimer.isEnabled()) {
                    if (rnd) {
                        Main.timerManager.setTimer(1.3f);
                    }
                    else {
                        Main.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.player);
        }
        else {
            if (this.oneTime) {
                Speed.mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.antiShake = false;
            this.speedOff();
        }
    }

    private void doAccel() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.player.onGround) {
            this.startY = Speed.mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.player) && !Speed.mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.isEnabled() && Speed.mc.player.getRidingEntity() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.player.posY >= this.startY + this.bounceHeight) {
                Speed.mc.player.motionY = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.isEnabled()) {
                    Main.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.player.posY == this.startY) {
                Speed.mc.player.motionY = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.175f;
                }
                if (this.useTimer.isEnabled()) {
                    if (rnd) {
                        Main.timerManager.setTimer(1.3f);
                    }
                    else {
                        Main.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.player);
        }
        else {
            if (this.oneTime) {
                Speed.mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }

    private void doOnground() {
        this.bounceHeight = 0.4;
        this.move = 0.26f;
        if (Speed.mc.player.onGround) {
            this.startY = Speed.mc.player.posY;
        }
        if (EntityUtil.getEntitySpeed((Entity)Speed.mc.player) <= 1.0) {
            this.lowChainVal = 1.0;
            this.highChainVal = 1.0;
        }
        if (EntityUtil.isEntityMoving((Entity)Speed.mc.player) && !Speed.mc.player.collidedHorizontally && !BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player) && BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            this.oneTime = true;
            this.antiShake = (this.noShake.isEnabled() && Speed.mc.player.getRidingEntity() == null);
            final Random random = new Random();
            final boolean rnd = random.nextBoolean();
            if (Speed.mc.player.posY >= this.startY + this.bounceHeight) {
                Speed.mc.player.motionY = -this.bounceHeight;
                ++this.lowChainVal;
                if (this.lowChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.lowChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.lowChainVal == 3.0) {
                    this.move = 0.275f;
                }
                if (this.lowChainVal == 4.0) {
                    this.move = 0.35f;
                }
                if (this.lowChainVal == 5.0) {
                    this.move = 0.375f;
                }
                if (this.lowChainVal == 6.0) {
                    this.move = 0.4f;
                }
                if (this.lowChainVal == 7.0) {
                    this.move = 0.425f;
                }
                if (this.lowChainVal == 8.0) {
                    this.move = 0.45f;
                }
                if (this.lowChainVal == 9.0) {
                    this.move = 0.475f;
                }
                if (this.lowChainVal == 10.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 11.0) {
                    this.move = 0.5f;
                }
                if (this.lowChainVal == 12.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 13.0) {
                    this.move = 0.525f;
                }
                if (this.lowChainVal == 14.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 15.0) {
                    this.move = 0.535f;
                }
                if (this.lowChainVal == 16.0) {
                    this.move = 0.545f;
                }
                if (this.lowChainVal >= 17.0) {
                    this.move = 0.545f;
                }
                if (this.useTimer.isEnabled()) {
                    Main.timerManager.setTimer(1.0f);
                }
            }
            if (Speed.mc.player.posY == this.startY) {
                Speed.mc.player.motionY = this.bounceHeight;
                ++this.highChainVal;
                if (this.highChainVal == 1.0) {
                    this.move = 0.075f;
                }
                if (this.highChainVal == 2.0) {
                    this.move = 0.175f;
                }
                if (this.highChainVal == 3.0) {
                    this.move = 0.375f;
                }
                if (this.highChainVal == 4.0) {
                    this.move = 0.6f;
                }
                if (this.highChainVal == 5.0) {
                    this.move = 0.775f;
                }
                if (this.highChainVal == 6.0) {
                    this.move = 0.825f;
                }
                if (this.highChainVal == 7.0) {
                    this.move = 0.875f;
                }
                if (this.highChainVal == 8.0) {
                    this.move = 0.925f;
                }
                if (this.highChainVal == 9.0) {
                    this.move = 0.975f;
                }
                if (this.highChainVal == 10.0) {
                    this.move = 1.05f;
                }
                if (this.highChainVal == 11.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 12.0) {
                    this.move = 1.1f;
                }
                if (this.highChainVal == 13.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 14.0) {
                    this.move = 1.15f;
                }
                if (this.highChainVal == 15.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal == 16.0) {
                    this.move = 1.175f;
                }
                if (this.highChainVal >= 17.0) {
                    this.move = 1.2f;
                }
                if (this.useTimer.isEnabled()) {
                    if (rnd) {
                        Main.timerManager.setTimer(1.3f);
                    }
                    else {
                        Main.timerManager.setTimer(1.0f);
                    }
                }
            }
            EntityUtil.moveEntityStrafe(this.move, (Entity)Speed.mc.player);
        }
        else {
            if (this.oneTime) {
                Speed.mc.player.motionY = -0.1;
                this.oneTime = false;
            }
            this.antiShake = false;
            this.highChainVal = 0.0;
            this.lowChainVal = 0.0;
            this.speedOff();
        }
    }

    @Override
    public void onDisable() {


            if (this.mode.getMode() == "ONGROUND" || this.mode.getMode() == "BOOST") {
            Speed.mc.player.motionY = -0.1;
        }
        Main.timerManager.setTimer(1.0f);
        this.highChainVal = 0.0;
        this.lowChainVal = 0.0;
        this.antiShake = false;
        super.onDisable();

    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        //if (this.mode.getMode() == "INSTANT") {
            //Speed.mc.player.motionY = -0.1;
        //}
    }


    @SubscribeEvent
    public void onMode(final PlayerMoveEvent event) {
        if (this.mode.getMode() == "INSTANT" && !fullNullCheck() && !Speed.mc.player.isSneaking() && !Speed.mc.player.isInWater() && !Speed.mc.player.isInLava() && (Speed.mc.player.movementInput.moveForward != 0.0f || Speed.mc.player.movementInput.moveStrafe != 0.0f)) {
            if (Speed.mc.player.onGround && this.strafeJump.isEnabled()) {
                event.setY(Speed.mc.player.motionY = 0.4);
            }
            final MovementInput movementInput = Speed.mc.player.movementInput;
            float moveForward = movementInput.moveForward;
            float moveStrafe = movementInput.moveStrafe;
            float rotationYaw = Speed.mc.player.rotationYaw;
            if (moveForward == 0.0 && moveStrafe == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            else {
                if (moveForward != 0.0) {
                    if (moveStrafe > 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? -45 : 45);
                    }
                    else if (moveStrafe < 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    moveForward = ((moveForward == 0.0f) ? moveForward : ((moveForward > 0.0) ? 1.0f : -1.0f));
                }
                moveStrafe = ((moveStrafe == 0.0f) ? moveStrafe : ((moveStrafe > 0.0) ? 1.0f : -1.0f));
                event.setX(moveForward * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + moveStrafe * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
                event.setZ(moveForward * EntityUtil.getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - moveStrafe * EntityUtil.getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
            }
        }
    }

    private void speedOff() {
        final float yaw = (float)Math.toRadians(Speed.mc.player.rotationYaw);
        if (BlockUtil.isBlockAboveEntitySolid((Entity)Speed.mc.player)) {
            if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindSneak.isKeyDown() && Speed.mc.player.onGround) {
                final EntityPlayerSP player = Speed.mc.player;
                player.motionX -= MathUtil.sin(yaw) * 0.15;
                final EntityPlayerSP player2 = Speed.mc.player;
                player2.motionZ += MathUtil.cos(yaw) * 0.15;
            }
        }
        else if (Speed.mc.player.collidedHorizontally) {
            if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindSneak.isKeyDown() && Speed.mc.player.onGround) {
                final EntityPlayerSP player3 = Speed.mc.player;
                player3.motionX -= MathUtil.sin(yaw) * 0.03;
                final EntityPlayerSP player4 = Speed.mc.player;
                player4.motionZ += MathUtil.cos(yaw) * 0.03;
            }
        }
        else if (!BlockUtil.isBlockBelowEntitySolid((Entity)Speed.mc.player)) {
            if (Speed.mc.gameSettings.keyBindForward.isKeyDown() && !Speed.mc.gameSettings.keyBindSneak.isKeyDown() && Speed.mc.player.onGround) {
                final EntityPlayerSP player5 = Speed.mc.player;
                player5.motionX -= MathUtil.sin(yaw) * 0.03;
                final EntityPlayerSP player6 = Speed.mc.player;
                player6.motionZ += MathUtil.cos(yaw) * 0.03;
            }
        }
        else {
            Speed.mc.player.motionX = 0.0;
            Speed.mc.player.motionZ = 0.0;
        }
    }




}
