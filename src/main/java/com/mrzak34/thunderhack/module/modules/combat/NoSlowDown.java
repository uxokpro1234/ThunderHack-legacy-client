package com.mrzak34.thunderhack.module.modules.combat;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoSlowDown extends Module {
    private Minecraft mc = Minecraft.getMinecraft();
    public BooleanSetting bows = new BooleanSetting("Bow", true);

    public NoSlowDown() {

        super("NoSlow2b2t", 0, com.mrzak34.thunderhack.module.Category.EXPLOIT, false);
        this.addSettings(bows);
    }
    private int food() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemFood) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private int bow() {
        int slot = 0;
        for (int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BOW) {
                slot = i;
                break;
            }
        }
        return slot;
    }
    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        {

            if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() instanceof ItemFood && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(this.food()));
            }
            if(this.bows.isEnabled())
                if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem() == Items.BOW && mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    mc.player.connection.sendPacket((Packet) new CPacketHeldItemChange(this.bow()));
                }
        }
    }
}

