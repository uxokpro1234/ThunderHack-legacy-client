package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.event.events.PlayerMoveEvent;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.EntityUtil;
import com.mrzak34.thunderhack.util.Timer;
import net.minecraft.block.BlockSlab;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraft.client.Minecraft;

public class ReverseStep extends Module {
    private Minecraft mc = Minecraft.getMinecraft();
    public static ModeSetting mode = new ModeSetting( "Mode", "PACKET", "PACKET", "MOTION", "VANILLA");
    public static NumberSetting height = new NumberSetting ( "Height", 5.0, 1.0, 5.0, 1.0);
    public static NumberSetting speed = new NumberSetting ( "Speed", 10, 1, 100, 1);
    public BooleanSetting wait = new BooleanSetting ( "Strict", true);
    public BooleanSetting cwater = new BooleanSetting("Water", true);
    public BooleanSetting clava = new BooleanSetting("Lava", true);

    private int fallTicks;
    private final Timer strictTimer = new Timer();
    public ReverseStep() {

        super("ReverseStep",0, Category.MOVEMENT,false);
        this.addSettings(mode,
                height,
                speed,
                wait,
                cwater,
                clava
        );
    }

    @SubscribeEvent
    public void onMotion(PlayerMoveEvent event) {
        if(mode.getMode() == "PACKET" || mode.getMode() == "MOTION"){
        // NCP will flag these as irregular movements
        if (EntityUtil.isInWater(mc.player) || mc.player.capabilities.isFlying || mc.player.isElytraFlying() || mc.player.isOnLadder()) {
            return;
        }

        // don't attempt to fast fall while jumping or sneaking
        if (mc.gameSettings.keyBindJump.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown() || Speed.getInstance().isToggled()) {
            return;
        }

        // fall start
        if (mc.player.onGround && mc.world.isAirBlock(new BlockPos(mc.player.getPositionVector()).down())) {
            fallTicks = 0;
        }

        // we are falling
        else {
            fallTicks++;
        }

        // we are stepping down a block
        if (mc.player.fallDistance > 0 && (fallTicks > 0 && fallTicks < 10)) {

            // nearest block below
            double fallingBlock = 0;
            for (double y = mc.player.posY; y > 0; y -= 0.001) {
                // cannot fall onto slabs
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock() instanceof BlockSlab) {
                    continue;
                }

                // check block state, if it's not null then it means something is there
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock().getDefaultState().getCollisionBoundingBox(mc.world, new BlockPos(0, 0, 0)) == null) {
                    continue;
                }

                fallingBlock = y;
                break;
            }

            // fall height
            double fallHeight = mc.player.posY - fallingBlock;

            // if we didn't find a block then we have a default value
            if (fallingBlock - mc.player.posY >= 0) {
                fallingBlock = mc.player.posY;
            }

            // check fall height
            if (fallHeight > height.getValue()) {
                return;
            }

            // check hole fall
            //if (!getCosmos().getHoleManager().isHole(new BlockPos(mc.player.posX, fallingBlock + 0.1, mc.player.posZ)) && hole.getValue()) {
            //return;
            //}

            switch (mode.getMode()) {
                case "MOTION":
                    // adjust player velocity
                    mc.player.connection.sendPacket(new CPacketPlayer(false));
                    mc.player.motionY = -speed.getValue() / 10;
                    break;
                case "PACKET":
                    // only attempt fast fall other second, prevents flag for SURVIVAL_FLY
                    if(strictTimer.passedMs(1000) && wait.isEnabled()) {

                        // send packets to simulate falling
                        if (mc.getConnection() != null) {
                            if (fallHeight > 0.5) {
                                mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.07840000152, mc.player.posZ, false));
                                mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.23363200604, mc.player.posZ, false));
                                mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.46415937495, mc.player.posZ, false));
                                mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 0.76847620241, mc.player.posZ, false));

                                if (fallHeight >= 1.5) {
                                    mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1.14510670065, mc.player.posZ, false));
                                    mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1.59260459764, mc.player.posZ, false));

                                    if (fallHeight >= 2.5) {
                                        mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 2.10955254674, mc.player.posZ, false));
                                        mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 2.69456154825, mc.player.posZ, false));

                                        if (fallHeight >= 3.5) {
                                            mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 3.34627038241, mc.player.posZ, false));
                                        }
                                    }
                                }
                            }
                        }

                        // speed up
                        // mc.player.motionY -= 0.08;
                        mc.player.setPosition(mc.player.posX, fallingBlock + 0.1, mc.player.posZ);
                        mc.player.setVelocity(0, 0, 0);
                        strictTimer.reset();
                    }

                    break;
            }
          }
        }
      }


    @SubscribeEvent
    public void onUpdateInput(final InputUpdateEvent event){
        if(mode.getMode() == "VANILLA"){
        if(mc.player.onGround) {
            mc.player.motionY = -speed.getValue() / 10;
            if (cwater.isEnabled()) {
                if (mc.player.isInWater()) {
                    mc.player.motionY = 0;
                }
            }

            if (clava.isEnabled()) {
                if (mc.player.isInLava()) {
                    mc.player.motionY = 0;
                }
            }


         }
        }
       }
      }
