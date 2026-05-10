package com.mrzak34.thunderhack.module.modules.combat;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoTotem extends Module {
    public BooleanSetting swapWhileMoving = new BooleanSetting("SwapWhileMoving",true);
    public BooleanSetting strict = new BooleanSetting("Strict",false);
    private int timer;

    public AutoTotem() {
        super("AutoTotem",0, Category.COMBAT,false);

        this.addSettings(swapWhileMoving,strict);
    }

    @Override
    public void onEnable() {
        this.timer = 0;
        super.onEnable();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if(timer > 0){
            timer--;
            return;
        }

        if(!swapWhileMoving.isEnabled()
                && (mc.player.movementInput.moveForward != 0
                || mc.player.movementInput.moveStrafe != 0))
            return;

        NonNullList<ItemStack> inv;
        ItemStack offhand = mc.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        int inventoryIndex;
        inv = mc.player.inventory.mainInventory;
        for(inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {

            if (inv.get(inventoryIndex) != ItemStack.EMPTY) {


                if ((offhand == null) || (offhand.getItem() != Items.TOTEM_OF_UNDYING)) {
                    if (inv.get(inventoryIndex).getItem() == Items.TOTEM_OF_UNDYING) {
                        replace(inventoryIndex);
                        break;
                    }
                }
            }
        }
    }

    public void replace(int inventoryIndex) {
        if (mc.player.openContainer instanceof ContainerPlayer) {
            mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);

        }
    }
}