package com.mrzak34.thunderhack.ui;

import com.mrzak34.thunderhack.Hud.Hud;
import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Collections;

public class HudOverlay extends Gui {

    private Minecraft mc = Minecraft.getMinecraft();
    public static class ModuleComparator implements Comparator<Module> {
        @Override
        public int compare(Module arg0, Module arg1){
            if(Minecraft.getMinecraft().fontRenderer.getStringWidth(arg0.getName()) > Minecraft.getMinecraft().fontRenderer.getStringWidth(arg1.getName())){
                return  -1;
            }
            if(Minecraft.getMinecraft().fontRenderer.getStringWidth(arg0.getName()) > Minecraft.getMinecraft().fontRenderer.getStringWidth(arg1.getName())){
                return  1;
            }
            return 0;
        }
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        Collections.sort(Main.moduleManager.modules, new ModuleComparator());
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer fr = mc.fontRenderer;
        final int[] counter = {1};

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            fr.drawStringWithShadow("Thunder Hack " + Main.VERSION, 2, 2, new Color(0, 255, 175).getRGB());
            fr.drawStringWithShadow("FPS: " + mc.getDebugFPS(), sr.getScaledWidth() - fr.getStringWidth("FPS: " + mc.getDebugFPS()) - 2, 2, rainbow(counter[0] * 300));
        }
        for(Hud h : Main.hudManager.getHudList()) {
                    if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
                        if (h.toggled) {
                        int y = 2;
                        fr.drawStringWithShadow("x: " + (new DecimalFormat("#0.00").format(mc.player.chasingPosX)), 2, y + fr.FONT_HEIGHT, -1);
                        y = y + fr.FONT_HEIGHT;
                        fr.drawStringWithShadow("y: " + (new DecimalFormat("#0.00").format(mc.player.chasingPosY)), 2, y + fr.FONT_HEIGHT, -1);
                        y = y + fr.FONT_HEIGHT;
                        fr.drawStringWithShadow("z: " + (new DecimalFormat("#0.00").format(mc.player.chasingPosZ)), 2, y + fr.FONT_HEIGHT, -1);
                    }
                }
        }

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            int y = sr.getScaledHeight();
            for (Module module : Main.moduleManager.getModuleList()) {
                if (module.isToggled()) {
                    fr.drawStringWithShadow(module.getName(), sr.getScaledWidth() - fr.getStringWidth(module.getName()) - 2, y - fr.FONT_HEIGHT, rainbow(counter[0] * 300));
                    y -= fr.FONT_HEIGHT;
                }
            }
        }
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.5f, 1f).getRGB();
    }
}
