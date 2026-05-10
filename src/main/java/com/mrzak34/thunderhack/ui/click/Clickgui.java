package com.mrzak34.thunderhack.ui.click;

import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.module.modules.client.ClickguiToggle;
import com.mrzak34.thunderhack.module.modules.client.Image;
import com.mrzak34.thunderhack.settings.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;

import static com.mrzak34.thunderhack.module.modules.render.RenderMethods.drawModalRect;
import static org.lwjgl.input.Mouse.getX;
import static org.lwjgl.input.Mouse.getY;

public class Clickgui extends GuiScreen {

    public int modules = 0;
    private int oldposX, oldposY;
    private final ResourceLocation lightning = new ResourceLocation("textures/lightning.png");
    private final ResourceLocation image = new ResourceLocation("textures/image.png");
    private final ResourceLocation image2 = new ResourceLocation("textures/image2.png");
    private final ResourceLocation THLogo = new ResourceLocation("textures/THLogo.png");
    private int progress;
    private boolean subOpen;

    public static void drawCompleteImage(float posX, float posY, int width, int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f((float) width, (float) height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float) width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static float calculateRotation(float var0) {
        if ((var0 %= 360.0F) >= 180.0F) {
            var0 -= 360.0F;
        }

        if (var0 < -180.0F) {
            var0 += 360.0F;
        }

        return var0;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        float psx = (float) Image.imposX.getValue() + 10;
        float psy = (float) Image.imposY.getValue();
        float mouseposx = (float) getX() / 100;
        float mouseposy = (float) getY() / 100;

        if (Image.toggled) {
            if (Image.mode.getMode() == "anime") {
                mc.getTextureManager().bindTexture(this.image);
                drawCompleteImage(psx - 1.0f + mouseposx, psy - 1.0f - mouseposy, (int) Image.Xscale.getValue(), (int) Image.Yscale.getValue());
            }
            if (Image.mode.getMode() == "anime2") {
                mc.getTextureManager().bindTexture(this.image2);
                drawCompleteImage(psx - 1.0f + mouseposx, psy - 1.0f - mouseposy, (int) Image.Xscale.getValue(), (int) Image.Yscale.getValue());
            }
            if (Image.mode.getMode() == "logo") {
                mc.getTextureManager().bindTexture(this.THLogo);
                drawCompleteImage(psx - 1.0f + mouseposx, psy - 1.0f - mouseposy, (int) Image.Xscale.getValue(), (int) Image.Yscale.getValue());
            }
        }

        FontRenderer fr = mc.fontRenderer;
        final int[] counter = {1};

        for(Category category : Category.values()) {
                if(category.mouseClicked) {
                    category.posX += mouseX - oldposX;
                    category.posY += mouseY - oldposY;
                }
            drawRect(category.posX - 2, category.posY + 11, category.posX + 100, category.posY - 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue()).getRGB());
            fr.drawStringWithShadow(category.name, category.posX, category.posY, -1);
            int posX = category.posX;
            int posY = category.posY + fr.FONT_HEIGHT+ 5;
            counter[0]++;
            for(Module m : Main.moduleManager.getModuleList()) {
                if(m.getCategory() == category && category.showModules) {
                    drawRect(posX - 2, posY + 11, posX + 100, posY - 3, new Color(0, 16, 17, 100).getRGB());
                    drawRect(posX - 1, posY + 10, posX + 99, posY - 2,m.isToggled() ? ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB() : new Color(0, 16, 17, 150).getRGB());
                    fr.drawStringWithShadow(m.getName(),posX + 1, posY, -1);

                    if (m.showSettings) {
                    }

                    this.progress = 0;

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
//              RenderMethods.glColor(new Color(0.0F, 0.0F, 100.0F, 1.0F));
                    mc.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/lightning.png"));
                    GlStateManager.translate(posX + 90, posY, 0.0F);
                    GlStateManager.rotate(calculateRotation((float)this.progress), 0.0F, 0.0F, 1.0F);
                    drawModalRect(-5, -5, 0.0F, 0.0F, 10, 10,  10,  10, 10.0f, 10.0f);
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    float height = 1.0f;
                    ++progress;

                    //mc.renderEngine.bindTexture(lightning);
                    //drawScaledCustomSizeModalRect(posX + 85, posY - 4, 0, 0, 16, 16, 16, 16, 16,16);
                    counter[0]++;

                    if(!m.settings.isEmpty()) {

                        for (Setting setting : m.settings) {

                            if (setting instanceof BooleanSetting && m.showSettings) {
                                BooleanSetting bool = (BooleanSetting) setting;
                                drawRect(posX - 2, posY + fr.FONT_HEIGHT + 16, posX - 1, posY + fr.FONT_HEIGHT + 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX - 1, posY + fr.FONT_HEIGHT + 16, posX + 100, posY + fr.FONT_HEIGHT + 2, new Color(0, 16, 17, 100).getRGB());
                                drawRect(posX, posY + fr.FONT_HEIGHT + 15, posX + 99, posY + fr.FONT_HEIGHT + 3,((BooleanSetting) setting).enabled  ? ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB() : new Color(0, 16, 17, 150).getRGB());
                                fr.drawStringWithShadow(setting.name, posX + 2, posY + fr.FONT_HEIGHT + 5, -1);
                                posY += fr.FONT_HEIGHT + 5;
                                counter[0]++;
                            }

                            if (setting instanceof NumberSetting && m.showSettings) {
                                NumberSetting number = (NumberSetting) setting;
                                drawRect(posX - 2, posY + fr.FONT_HEIGHT + 16, posX - 1, posY + fr.FONT_HEIGHT + 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX - 1, posY + fr.FONT_HEIGHT + 16, posX + 100, posY + fr.FONT_HEIGHT + 2, new Color(0, 16, 17, 100).getRGB());
                                drawRect(posX, posY + fr.FONT_HEIGHT + 15,posX + ((int)((NumberSetting) setting).value * 99) / ((int) ((NumberSetting) setting).maximum), posY + fr.FONT_HEIGHT + 3, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX + ((int)((NumberSetting) setting).value * 98) / ((int) ((NumberSetting) setting).maximum), posY + fr.FONT_HEIGHT + 15, posX + 99, posY + fr.FONT_HEIGHT + 3, new Color(0, 16, 17, 150).getRGB());
                                fr.drawStringWithShadow(setting.name + ":" + (new DecimalFormat("#0").format(number.getValue())), posX + 2, posY + fr.FONT_HEIGHT + 5, -1);
                                posY += fr.FONT_HEIGHT + 5;
                                counter[0]++;
                            }

                            if (setting instanceof ModeSetting && m.showSettings) {
                                ModeSetting mode = (ModeSetting) setting;
                                drawRect(posX - 2, posY + fr.FONT_HEIGHT + 16, posX - 1, posY + fr.FONT_HEIGHT + 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX - 1, posY + fr.FONT_HEIGHT + 16, posX + 100, posY + fr.FONT_HEIGHT + 2, new Color(0, 16, 17, 100).getRGB());
                                drawRect(posX, posY + fr.FONT_HEIGHT + 15, posX + 99, posY + fr.FONT_HEIGHT + 3, new Color(0, 16, 17, 150).getRGB());
                                fr.drawStringWithShadow(setting.name + ":" + mode.getMode(), posX + 2, posY + fr.FONT_HEIGHT + 5, -1);
                                posY += fr.FONT_HEIGHT + 5;
                                counter[0]++;
                            }
                            if (setting instanceof KeybindSetting && m.showSettings) {
                                KeybindSetting keyBind = (KeybindSetting) setting;
                                drawRect(posX - 2, posY + fr.FONT_HEIGHT + 16, posX - 1, posY + fr.FONT_HEIGHT + 2, ClickguiToggle.rainbow.isEnabled() ? rainbow(counter[0] * 300) : new Color((int) ClickguiToggle.red.getValue(), (int) ClickguiToggle.green.getValue(), (int) ClickguiToggle.blue.getValue(), 150).getRGB());
                                drawRect(posX - 1, posY + fr.FONT_HEIGHT + 16, posX + 100, posY + fr.FONT_HEIGHT + 2, new Color(0, 16, 17, 100).getRGB());
                                drawRect(posX, posY + fr.FONT_HEIGHT + 15, posX + 99, posY + fr.FONT_HEIGHT + 3, new Color(0, 16, 17, 150).getRGB());
                                fr.drawStringWithShadow(Keyboard.getKeyName(keyBind.code), posX + 2, posY + fr.FONT_HEIGHT + 5, -1);
                                posY += fr.FONT_HEIGHT + 5;
                                counter[0]++;
                            }
                        }
                    }
                    posY += fr.FONT_HEIGHT + 5;
                    counter[0]++;
                }
            }
            counter[0]++;
            fr.drawStringWithShadow(String.valueOf(modules), category.posX + 99 - fr.getStringWidth(String.valueOf(modules)), category.posY, -1);
        }
        counter[0]++;
        oldposX = mouseX;
        oldposY = mouseY;

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        FontRenderer fr = mc.fontRenderer;
        for(Category category : Category.values()) {
            int posX = category.posX;
            int posY = category.posY + fr.FONT_HEIGHT + 5;
            for(Module m : Main.moduleManager.getModuleList()) {
                if(m.getCategory() == category) {
                    if(mouseX >= posX - 2 && mouseX <= posX + 100 && mouseY >= posY - 3 && mouseY <= posY + 8) {
                        if(mouseButton == 0) {
                            m.toggle();
                        }
                        if(mouseButton == 1) {
                            if(m.showSettings) {
                                m.showSettings = false;
                            } else {
                                m.showSettings = true;
                            }
                        }
                    }
                    for(Setting setting : m.settings) {
                        if(m.showSettings) {
                            posY += fr.FONT_HEIGHT + 5;
                        }
                            if(mouseX >= posX - 2 && mouseX <= posX + 100 && mouseY >= posY - 3 && mouseY <= posY + 8) {
                                if (mouseButton == 0) {
                                    if (setting instanceof BooleanSetting && m.showSettings) {
                                        ((BooleanSetting) setting).toggle();
                                        posY += fr.FONT_HEIGHT + 5;
                                    }
                                }
                                if (mouseButton == 0) {
                                    if (setting instanceof ModeSetting && m.showSettings) {
                                        ((ModeSetting) setting).cycle();
                                        posY += fr.FONT_HEIGHT + 5;
                                    }
                                }
                                if (setting instanceof NumberSetting && m.showSettings) {
                                    if(mouseX >= posX && mouseX <= posX + 98 && mouseY >= posY && mouseY <= posY + 5) {
                                        if (mouseButton == 0) {
                                            ((NumberSetting) setting).value = ((((mouseX - posX) * ((NumberSetting) setting).increment * ((NumberSetting) setting).maximum) - ((mouseX - posX) * ((NumberSetting) setting).minimum)) / 98 + ((NumberSetting) setting).minimum);
                                        }
                                        posY += fr.FONT_HEIGHT + 5;
                                    }
                                }
                                if ((setting instanceof KeybindSetting) && m.showSettings) {
                                    boolean keybind = false;
                                    if (mouseButton == 0) {
                                        keybind = true;
                                        if (Keyboard.getEventKeyState()){
                                            ((KeybindSetting) setting).code = Keyboard.getEventKey();
                                            keybind = false;
                                        }
                                    }
                                }
                            }
                    }
                    posY += fr.FONT_HEIGHT + 5;
                }
            }
            if(mouseX >= category.posX - 2 && mouseX <= category.posX + 100 && mouseY >= category.posY - 2 && mouseY - 3 <= category.posY + 8) {
                if(mouseButton == 0) {
                    category.mouseClicked = true;
                }
                if(mouseButton == 1) {
                    if(category.showModules) {
                        category.showModules = false;
                    } else {
                        category.showModules = true;
                    }
                }
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen) null);

            Main.INSTANCE.clickGUI.setToggled(false);

            for(Category category : Category.values()) {
                category.mouseClicked = false;

            }

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(Category category : Category.values()) {
            category.mouseClicked = false;
        }

    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / (int) ClickguiToggle.rainbowdelay.getValue());
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 0.5f, 1f).getRGB();
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

}