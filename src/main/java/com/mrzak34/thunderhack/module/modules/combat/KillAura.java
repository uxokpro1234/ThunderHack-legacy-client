package com.mrzak34.thunderhack.module.modules.combat;

import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;

import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.DamageUtil;
import com.mrzak34.thunderhack.util.EntityUtil;
import com.mrzak34.thunderhack.util.MathUtil;
import com.mrzak34.thunderhack.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

import java.util.Comparator;



public class KillAura extends Module {
    public NumberSetting range = new NumberSetting("Range", 40.0f, 1.0f, 60.0f, 1.0f);
    public BooleanSetting packet = new BooleanSetting("Packet", false);



    private static Minecraft mc = Minecraft.getMinecraft();

    public KillAura() {
        super("KillAura", 0, Category.COMBAT, false);
        this.setKey(Keyboard.KEY_P);
        this.addSettings(range);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {


        EntityPlayer target = mc.world.playerEntities.stream().filter(entityPlayer -> entityPlayer != mc.player).min(Comparator.comparing(entityPlayer -> entityPlayer.getDistance(mc.player))).filter(entityPlayer -> entityPlayer.getDistance(mc.player) <=  range.getValue() / 10.0f).orElse(null);

        if (target != null) {
            Main.rotationManager.lookAtEntity(target);
            if (mc.player.getCooledAttackStrength(0) == 1) {
                EntityUtil.attackEntity(target, packet.isEnabled(), true);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.resetCooldown();
            }
        }
    }
}
