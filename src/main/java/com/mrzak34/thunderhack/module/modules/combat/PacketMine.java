package com.mrzak34.thunderhack.module.modules.combat;

import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.event.events.EventPlayerDamageBlock;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.BlockUtil;
import com.mrzak34.thunderhack.util.RenderUtil;
import com.mrzak34.thunderhack.util.Timer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.init.Items;
import java.awt.*;

public class PacketMine extends Module {
    public Timer timer = new Timer();
    private static Minecraft mc = Minecraft.getMinecraft();
    public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "Full");
    public ModeSetting silent = new ModeSetting("Silent", "None", "Pickaxe", "Sword", "Showel", "None");
    public BooleanSetting render = new BooleanSetting("Render", true);
    public NumberSetting lineWidth = new NumberSetting("Line", 1f, 1f, 50f, 1f);
    public BooleanSetting outline = new BooleanSetting("Outlie", true);
    public BooleanSetting box = new BooleanSetting("Box", true);
    public NumberSetting boxAlpha = new NumberSetting("BoxAlpha", 85, 1, 255, 1);

    public PacketMine() {
        super("PacketMine", 0, Category.EXPLOIT, false);
        this.addSettings(mode, silent, render, lineWidth, outline, box, boxAlpha);
    }

    public BlockPos currentPos;
    public IBlockState currentBlockState;

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (currentPos != null) {
            if (!PacketMine.mc.world.getBlockState(currentPos).equals(currentBlockState) || PacketMine.mc.world.getBlockState(currentPos).getBlock() == Blocks.AIR) {
                currentPos = null;
                currentBlockState = null;
            }
        }
    }

    //silentpick
    private int getPickSlot() {
        for (int i = 0; i < 9; ++i) {
            if (PacketMine.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemPickaxe) continue;
            return i;
        }
        return -1;
    }

    //silentsword
    private int getSwoSlot() {
        for (int a = 0; a < 9; ++a) {
            if (PacketMine.mc.player.inventory.getStackInSlot(a).getItem() instanceof ItemSword) continue;
            return a;
        }
        return -1;
    }

    //silentshovel
    private int getShSlot() {
        for (int b = 0; b < 9; ++b) {
            if (PacketMine.mc.player.inventory.getStackInSlot(b).getItem() != Items.DIAMOND_SHOVEL) continue;
            return b;
        }
        return -1;
    }


    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        PacketMine.mc.playerController.blockHitDelay = 0;
    }


    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (render.isEnabled() && this.currentPos != null) {
            Color color = new Color(102, 4, 227, 255);
            RenderUtil.drawBoxESP(currentPos, color, false, color, (float) lineWidth.getValue(), outline.isEnabled(), box.isEnabled(), (int) boxAlpha.getValue(), false);
        }
    }

    @SubscribeEvent
    public void onBlockEvent(EventPlayerDamageBlock event) {
        if (PacketMine.mc.playerController.curBlockDamageMP > 0.01f) {
            PacketMine.mc.playerController.isHittingBlock = true;
        }

        if (PacketMine.mc.player != null && silent.getMode() == "Pickaxe" && getPickSlot() != -1) {
            PacketMine.mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(getPickSlot()));
        }
            if (BlockUtil.canBreak(event.getPos())) {
                switch (mode.getMode()) {
                    case "Full": {
                        if (currentPos == null) {
                            currentPos = event.getPos();
                            currentBlockState = PacketMine.mc.world.getBlockState(currentPos);
                        }
                        PacketMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getDirection()));
                        PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getDirection()));
                        event.setCanceled(true);
                        break;
                    }

                    case "Packet": {
                        if (currentPos == null) {
                            currentPos = event.getPos();
                            currentBlockState = PacketMine.mc.world.getBlockState(currentPos);
                        }
                        PacketMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        PacketMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getDirection()));
                        event.setCanceled(true);
                    }
                }
            }
        }
    }