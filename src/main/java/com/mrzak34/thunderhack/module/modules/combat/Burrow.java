package com.mrzak34.thunderhack.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.mrzak34.thunderhack.command.Command;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.BurrowUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static net.minecraft.network.play.client.CPacketEntityAction.Action.START_SNEAKING;
import static net.minecraft.network.play.client.CPacketEntityAction.Action.STOP_SNEAKING;


public class Burrow extends Module {
    private Minecraft mc = Minecraft.getMinecraft();
    public BooleanSetting rotate = new BooleanSetting("Rotate",true);
    public NumberSetting offset = new NumberSetting("Offset",-9.0F, -45.0F, 45.0F, 1);
    public BooleanSetting sneak = new BooleanSetting("Sneak",true);
    public ModeSetting sblock = new ModeSetting("Mode","OBSIDIAN", "OBSIDIAN", "ECHEST");

    private BlockPos originalPos;
    private int oldSlot = -1;

    public Burrow() {
        super("Burrow", 0, Category.COMBAT, false);
        this.addSettings(rotate,offset,sneak,sblock);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // Save our original pos
        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        // If we can't place in our actual post then toggle and return
        if(sblock.getMode() == "OBSIDIAN")
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
                    intersectsWithEntity(this.originalPos)) {
                toggle();
                return;
            }
        if(sblock.getMode() == "ECHEST")
            if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.ENDER_CHEST) ||
                    intersectsWithEntity(this.originalPos)) {
                toggle();
                return;
            }
        // Save our item slot
        oldSlot = mc.player.inventory.currentItem;
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        // If we don't have obsidian in hotbar toggle and return
        if(sblock.getMode() == "OBSIDIAN")
            if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) {
                Command.sendSilentMessage(ChatFormatting.BOLD + "<Burrow>" + " " + ChatFormatting.RED + "Can't find obby in hotbar!!");
                toggle();
                return;
            }
        if(sblock.getMode() == "ECHEST")
            if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
                Command.sendSilentMessage(ChatFormatting.BOLD + "<Burrow>" + " " + ChatFormatting.RED + "Can't find echest in hotbar!!");
                toggle();
                return;
            }
        // Change to obsidian slot
        if(sblock.getMode() == "OBSIDIAN")
            BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockObsidian.class));

        if(sblock.getMode() == "ECHEST")
            BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockEnderChest.class));

        // Fake jump
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));

        // Sneak option.
        boolean sneaking = mc.player.isSneaking();
        if (sneak.isEnabled()) {
            if (sneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, START_SNEAKING));
            }
        }

        // Place block
        BurrowUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.isEnabled(), true, false);

        // Rubberband
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));

        // SwitchBack
        BurrowUtil.switchToSlot(oldSlot);

        // Stop sneak if the option was enabled.
        if (sneak.isEnabled()) {
            if (sneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, STOP_SNEAKING));
            }
        }
        // AutoDisable
        toggle();
    };

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
}