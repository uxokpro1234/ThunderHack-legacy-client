package com.mrzak34.thunderhack.module.modules.combat;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;

import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import java.util.Comparator;


public class AutoAim extends Module {
    public BooleanSetting Rotations = new BooleanSetting("Rotations", true);
    public NumberSetting rng = new NumberSetting("Range",50, 1, 100, 1);
    public NumberSetting deg = new NumberSetting("Degree",110, 1, 180, 1);

    private static Minecraft mc = Minecraft.getMinecraft();
    public AutoAim() {
        super("AutoAim",0, Category.COMBAT, false);
        this.setKey(Keyboard.KEY_NONE);
        this.addSettings(Rotations,rng, deg);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {


        if(Rotations.isEnabled()){
            float range = (float) rng.getValue() / 10;
            EntityPlayer target = mc.world.playerEntities.stream().filter(entityPlayer -> entityPlayer != mc.player).min(Comparator.comparing(entityPlayer -> entityPlayer.getDistance(mc.player))).filter(entityPlayer -> entityPlayer.getDistance(mc.player) <= range).orElse(null);
            if (target != null) {
                mc.player.rotationYaw = rotations(target)[0];
                mc.player.rotationPitch = rotations(target)[1];


            }
        }
        float range = (float) rng.getValue() / 10;

        super.onEnable();

    }
    public float[] rotations(Entity entity) {
        double d0 = entity.posX - mc.player.posX;
        double d1 = entity.posY - (mc.player.posY + (double) mc.player.getEyeHeight());
        double d2 = entity.posZ - mc.player.posZ;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
        float f1 = (float)(-(MathHelper.atan2(d1, d3) * (deg.getValue() / Math.PI)));

        return new float[]{f, f1};
    }

}

