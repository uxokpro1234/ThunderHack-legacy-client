package com.mrzak34.thunderhack.ui.click;

import com.mrzak34.thunderhack.Hud.Hud;
import com.mrzak34.thunderhack.Hud.HudMenu;
import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.module.modules.client.ClickguiToggle;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.settings.Setting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;

import static com.mrzak34.thunderhack.Hud.HudElements.Position.*;

public class Hudeditor extends GuiScreen {

    private int oldposX, oldposY;
    private final ResourceLocation lightning = new ResourceLocation("textures/lightning.png");
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        FontRenderer fr = mc.fontRenderer;

        for(HudMenu hudmenu : HudMenu.values()) {
            final int[] counter = {1};
            if(hudmenu.mouseClicked) {
                hudmenu.posX += mouseX - oldposX;
                hudmenu.posY += mouseY - oldposY;
            }
            drawRect(hudmenu.posX - 2, hudmenu.posY + 11, hudmenu.posX + 100, hudmenu.posY - 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue()).getRGB());
            fr.drawStringWithShadow(hudmenu.name, hudmenu.posX, hudmenu.posY, -1);
            int posX = hudmenu.posX;
            int posY = hudmenu.posY + fr.FONT_HEIGHT+ 5;
            for(Hud h : Main.hudManager.getHudList()) {
                if(h.getHud() == hudmenu && hudmenu.showHuds) {
                    drawRect(posX - 2, posY + 11, posX + 100, posY - 3, new Color(0, 16, 17, 100).getRGB());
                    drawRect(posX - 1, posY + 10, posX + 99, posY - 2,h.isToggled() ? ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB() : new Color(0, 16, 17, 150).getRGB());
                    fr.drawStringWithShadow(h.getName(),posX + 1, posY, -1);
                    mc.renderEngine.bindTexture(lightning);
                    drawScaledCustomSizeModalRect(posX + 85, posY - 4, 0, 0, 16, 16, 16, 16, 16,16);

                    if(!h.settings.isEmpty()) {
                        for (Setting setting : h.settings) {
                            if (setting instanceof BooleanSetting && h.showSettings) {
                                BooleanSetting bool = (BooleanSetting) setting;
                                drawRect(posX - 2, posY + fr.FONT_HEIGHT + 16, posX - 1, posY + fr.FONT_HEIGHT + 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX - 1, posY + fr.FONT_HEIGHT + 16, posX + 100, posY + fr.FONT_HEIGHT + 2, new Color(0, 16, 17, 100).getRGB());
                                drawRect(posX, posY + fr.FONT_HEIGHT + 15, posX + 99, posY + fr.FONT_HEIGHT + 3,((BooleanSetting) setting).enabled  ? ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB() : new Color(0, 16, 17, 150).getRGB());
                                fr.drawStringWithShadow(setting.name, posX + 2, posY + fr.FONT_HEIGHT + 5, -1);
                                posY += fr.FONT_HEIGHT + 5;
                                counter[0]++;
                            }

                            if (setting instanceof NumberSetting && h.showSettings) {
                                NumberSetting number = (NumberSetting) setting;
                                drawRect(posX - 2, posY + fr.FONT_HEIGHT + 16, posX - 1, posY + fr.FONT_HEIGHT + 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX - 1, posY + fr.FONT_HEIGHT + 16, posX + 100, posY + fr.FONT_HEIGHT + 2, new Color(0, 16, 17, 100).getRGB());
                                drawRect(posX, posY + fr.FONT_HEIGHT + 15,posX + ((int)((NumberSetting) setting).value * 99) / ((int) ((NumberSetting) setting).maximum), posY + fr.FONT_HEIGHT + 3, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX + ((int)((NumberSetting) setting).value * 98) / ((int) ((NumberSetting) setting).maximum), posY + fr.FONT_HEIGHT + 15, posX + 99, posY + fr.FONT_HEIGHT + 3, new Color(0, 16, 17, 150).getRGB());
                                fr.drawStringWithShadow(setting.name + ":" + (new DecimalFormat("#0").format(number.getValue())), posX + 2, posY + fr.FONT_HEIGHT + 5, -1);
                                posY += fr.FONT_HEIGHT + 5;
                                counter[0]++;
                            }

                            if (setting instanceof ModeSetting && h.showSettings) {
                                ModeSetting mode = (ModeSetting) setting;
                                drawRect(posX - 2, posY + fr.FONT_HEIGHT + 16, posX - 1, posY + fr.FONT_HEIGHT + 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX - 1, posY + fr.FONT_HEIGHT + 16, posX + 100, posY + fr.FONT_HEIGHT + 2, new Color(0, 16, 17, 100).getRGB());
                                drawRect(posX, posY + fr.FONT_HEIGHT + 15, posX + 99, posY + fr.FONT_HEIGHT + 3, new Color(0, 16, 17, 150).getRGB());
                                fr.drawStringWithShadow(setting.name + ":" + mode.getMode(), posX + 2, posY + fr.FONT_HEIGHT + 5, -1);
                                posY += fr.FONT_HEIGHT + 5;
                                counter[0]++;
                            }
                        }
                    }
                    posY += fr.FONT_HEIGHT + 5;
                    counter[0]++;
                }
                counter[0]++;
            }
            counter[0]++;

            for(Hud h : Main.hudManager.getHudList()) {
                xpos += mouseX - oldposX;
                ypos += mouseY - oldposY;
                    if(h.getHud() == hudmenu) {
                        if(h.toggled){
                        drawRect(xpos, ypos, xpos + 30, ypos + 20, new Color(0, 16, 17, 100).getRGB());
                    }
                }
            }
        }

        oldposX = mouseX;
        oldposY = mouseY;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        FontRenderer fr = mc.fontRenderer;
        for(HudMenu hudmenu : HudMenu.values()) {
            int posX = hudmenu.posX;
            int posY = hudmenu.posY + fr.FONT_HEIGHT + 5;
            xpos += mouseX - oldposX;
            ypos += mouseY - oldposY;
            for(Hud h : Main.hudManager.getHudList()) {
                if(h.getHud() == hudmenu) {
                    if(mouseX >= posX - 2 && mouseX <= posX + 100 && mouseY >= posY - 3 && mouseY <= posY + 8) {
                        if(mouseButton == 0) {
                            h.toggle();
                        }
                        if(mouseButton == 1) {
                            if(h.showSettings) {
                                h.showSettings = false;
                            } else {
                                h.showSettings = true;
                            }
                        }
                    }
                    for(Setting setting : h.settings) {
                        if(h.showSettings) {
                            posY += fr.FONT_HEIGHT + 5;
                        }
                        if(mouseX >= posX - 2 && mouseX <= posX + 100 && mouseY >= posY - 3 && mouseY <= posY + 8) {
                            if (mouseButton == 0) {
                                if (setting instanceof BooleanSetting && h.showSettings) {
                                    ((BooleanSetting) setting).toggle();
                                    posY += fr.FONT_HEIGHT + 5;
                                }
                            }
                            if (mouseButton == 0) {
                                if (setting instanceof ModeSetting && h.showSettings) {
                                    ((ModeSetting) setting).cycle();
                                    posY += fr.FONT_HEIGHT + 5;
                                }
                            }
                            if (setting instanceof NumberSetting && h.showSettings) {
                                if(mouseX >= posX && mouseX <= posX + 98 && mouseY >= posY && mouseY <= posY + 5) {
                                    if (mouseButton == 0) {
                                        ((NumberSetting) setting).value = ((((mouseX - posX) * ((NumberSetting) setting).increment * ((NumberSetting) setting).maximum) - ((mouseX - posX) * ((NumberSetting) setting).minimum)) / 98 + ((NumberSetting) setting).minimum);
                                    }
                                    posY += fr.FONT_HEIGHT + 5;
                                }
                            }
                        }
                    }
                    posY += fr.FONT_HEIGHT + 5;
                }
            }
            if(mouseX >= hudmenu.posX - 2 && mouseX <= hudmenu.posX + 100 && mouseY >= hudmenu.posY - 2 && mouseY - 3 <= hudmenu.posY + 8) {
                if(mouseButton == 0) {
                    hudmenu.mouseClicked = true;
                }else {
                    hudmenu.mouseClicked = false;
                }
                if(mouseButton == 1) {
                    if(hudmenu.showHuds) {
                        hudmenu.showHuds = false;
                    }
                }
            }
        }

        if(mouseX >= xpos && mouseX <= xpos + 30 && mouseY >= ypos && mouseY <= ypos + 20) {
            if(mouseButton == 0) {
                mouseClicked = true;
            }
        }

    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen) null);

            Main.INSTANCE.clickGUI.setToggled(false);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(HudMenu hudManager : HudMenu.values()) {
            hudManager.mouseClicked = false;
        }
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.5f, 1f).getRGB();
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

}
