package com.mrzak34.thunderhack.module.modules.render;

import com.mrzak34.thunderhack.Main;
import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.DamageUtil;
import com.mrzak34.thunderhack.util.EntityUtil;
import com.mrzak34.thunderhack.util.RenderUtil;
import com.mrzak34.thunderhack.util.RotationUtil;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Objects;

import static net.minecraft.client.gui.Gui.drawScaledCustomSizeModalRect;

public class Nametags extends Module {

    public static BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
    public static ModeSetting mode = new ModeSetting("mode", "minimal", "full", "minimal", "none");
    public static BooleanSetting outline = new BooleanSetting("outline", true);
    public static BooleanSetting onlyFov = new BooleanSetting("OnlyFov", false);
    public static NumberSetting lineWidth = new NumberSetting("LineWidth", 10, 0, 10, 1);
    public static NumberSetting redSetting = new NumberSetting("red", 10, 0, 255, 1);
    public static NumberSetting greenSetting = new NumberSetting("green", 10, 0, 255, 1);
    public static NumberSetting blueSetting = new NumberSetting("blue", 10, 0, 255, 1);
    public static NumberSetting alphaSetting = new NumberSetting("alpha", 100, 0, 255, 1);
    public static BooleanSetting health = new BooleanSetting("health", true);
    public static NumberSetting scaling = new NumberSetting("scaling", 10, 0, 10, 1);
    public static BooleanSetting scaleing = new BooleanSetting("scaleing", true);
    public static BooleanSetting smartScale = new BooleanSetting("smartScale", true);
    public static NumberSetting factor = new NumberSetting("Factor", 2, 1, 10, 1);
    public static BooleanSetting ping = new BooleanSetting("Ping", true);
    public static BooleanSetting totemPops = new BooleanSetting("TotemPops", false);
    public static BooleanSetting gamemode = new BooleanSetting("Gamemode", true);
    public static BooleanSetting entityID = new BooleanSetting("EntityID", false);
    public static BooleanSetting head = new BooleanSetting("Head", true);
    public static BooleanSetting enabled = new BooleanSetting("enabled", false);
    public static BooleanSetting heldStackName = new BooleanSetting("HeldStackName", true);
    public static BooleanSetting armor = new BooleanSetting("Armor", true);

    public Nametags() {
        super("Nametags", 0, Category.RENDER, false);
        this.addSettings(invisibles, outline, onlyFov, lineWidth, redSetting, greenSetting, blueSetting, alphaSetting, health,
                scaling, smartScale, factor, ping, totemPops, gamemode, entityID, head, armor, heldStackName, mode);
    }
    private static Nametags INSTANCE = new Nametags();
    public static Nametags getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Nametags();
        }
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!fullNullCheck()) {
            for (final EntityPlayer player : Nametags.mc.world.playerEntities) {
                if (player != null && !player.equals((Object)Nametags.mc.player) && player.isEntityAlive() && (!player.isInvisible() || this.invisibles.isEnabled())) {
                    if (this.onlyFov.isEnabled() && RotationUtil.isInFov((Entity)player)) {
                        continue;
                    }
                    final double x = this.interpolate(player.lastTickPosX,  player.posX,  event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosX;
                    final double y = this.interpolate(player.lastTickPosY,  player.posY,  event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosY;
                    final double z = this.interpolate(player.lastTickPosZ,  player.posZ,  event.getPartialTicks()) - Nametags.mc.getRenderManager().renderPosZ;
                    this.renderNameTag(player,  x,  y,  z,  event.getPartialTicks());
                }
            }
        }
    }

    public void drawRect(final float x,  final float y,  final float w,  final float h,  final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.lineWidth.getValue() / 10);
        GlStateManager.tryBlendFuncSeparate(770,  771,  1,  0);
        bufferbuilder.begin(7,  DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x,  (double)h,  0.0).color(red,  green,  blue,  alpha).endVertex();
        bufferbuilder.pos((double)w,  (double)h,  0.0).color(red,  green,  blue,  alpha).endVertex();
        bufferbuilder.pos((double)w,  (double)y,  0.0).color(red,  green,  blue,  alpha).endVertex();
        bufferbuilder.pos((double)x,  (double)y,  0.0).color(red,  green,  blue,  alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public void drawOutlineRect(final float x,  final float y,  final float w,  final float h,  final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth((float)this.lineWidth.getValue() / 10);
        GlStateManager.tryBlendFuncSeparate(770,  771,  1,  0);
        bufferbuilder.begin(2,  DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x,  (double)h,  0.0).color(red,  green,  blue,  alpha).endVertex();
        bufferbuilder.pos((double)w,  (double)h,  0.0).color(red,  green,  blue,  alpha).endVertex();
        bufferbuilder.pos((double)w,  (double)y,  0.0).color(red,  green,  blue,  alpha).endVertex();
        bufferbuilder.pos((double)x,  (double)y,  0.0).color(red,  green,  blue,  alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private void renderNameTag(final EntityPlayer player,  final double x,  final double y,  final double z,  final float delta) {
        Nametags.enabled.isEnabled();
        FontRenderer fr = mc.fontRenderer;
        double tempY = y;
        tempY += (player.isSneaking() ? 0.5 : 0.7);
        final Entity camera = Nametags.mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX,  camera.posX,  delta);
        camera.posY = this.interpolate(camera.prevPosY,  camera.posY,  delta);
        camera.posZ = this.interpolate(camera.prevPosZ,  camera.posZ,  delta);
        final String displayTag = this.getDisplayTag(player);
        final double distance = camera.getDistance(x + Nametags.mc.getRenderManager().viewerPosX,  y + Nametags.mc.getRenderManager().viewerPosY,  z + Nametags.mc.getRenderManager().viewerPosZ);
        final int width = (fr.getStringWidth(displayTag)) / 2;
        double scale = (0.0018 + this.scaling.getValue() * (distance * this.factor.getValue())) / 10000;
        if (distance <= 8.0 && this.smartScale.isEnabled()) {
            scale = 0.0245;
        }
        if (!this.scaleing.isEnabled()) {
            scale = this.scaling.getValue() / 100.0;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f,  -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x,  (float)tempY + 1.4f,  (float)z);
        GlStateManager.rotate(-Nametags.mc.getRenderManager().playerViewY,  0.0f,  1.0f,  0.0f);
        GlStateManager.rotate(Nametags.mc.getRenderManager().playerViewX,  (Nametags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f,  0.0f,  0.0f);
        GlStateManager.scale(-scale,  -scale,  scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();

        if (head.isEnabled()) {
            try {
                drawHead(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getLocationSkin(), -(width + 16), -12);
            } catch (Exception exception) {}
        }

        if (this.outline.isEnabled()) {
            final int color = new Color((int) redSetting.getValue(),(int) greenSetting.getValue(),(int) blueSetting.getValue(),(int) alphaSetting.getValue()).getRGB();
            this.drawRect(-width - 2, -(Nametags.mc.fontRenderer.FONT_HEIGHT + 3), width,  1.5f, color);
            this.drawOutlineRect( -width - 2, -(Nametags.mc.fontRenderer.FONT_HEIGHT + 3),  width,  1.5f,  new Color(0, 0, 0, 255).getRGB());
        }
        this.drawStringWithShadow(displayTag, -width - 1, -10, -1);
        drawSmoothRect((float) -width, -1, -width - 2 + width * 2, 0, new Color(30, 30, 30).getRGB());
        drawSmoothRect((float) -width, -1, -width - 2 + (width * 2 / player.getMaxHealth() * player.getHealth()), 0, new Color(0, 255, 0).getRGB());
        if (EntityUtil.getHealth((Entity)player) > 20) {
            drawSmoothRect((float) -width, -1, -width - 2 + (width * 2 / player.getMaxHealth() * ((EntityUtil.getHealth((Entity)player) - 20))), 0, new Color(255, 255, 0).getRGB());
        }
        GlStateManager.disableBlend();
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
        }
        if (this.heldStackName.isEnabled() && !renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
            final String stackName = renderMainHand.getDisplayName();
            final int stackNameWidth = fr.getStringWidth(stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75f,  0.75f,  0.0f);
            this.drawStringWithShadow(stackName,  (float)(-stackNameWidth),  -(this.getBiggestArmorTag(player) + 20.0f),  -1);
            GL11.glScalef(1.5f,  1.5f,  1.0f);
            GL11.glPopMatrix();
        }
        if (this.armor.isEnabled()) {
            GlStateManager.pushMatrix();
            int xOffset = -8;
            for (final ItemStack stack : player.inventory.armorInventory) {
                if (stack == null) {
                    continue;
                }
                xOffset -= 8;
            }
            xOffset -= 8;
            final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
            if (renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
                renderOffhand.stackSize = 1;
            }
            this.renderItemStack(renderOffhand,  xOffset);
            xOffset += 16;
            for (final ItemStack stack2 : player.inventory.armorInventory) {
                if (stack2 == null) {
                    continue;
                }
                final ItemStack armourStack = stack2.copy();
                if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                    armourStack.stackSize = 1;
                }
                this.renderItemStack(armourStack,  xOffset);
                xOffset += 16;
            }
            this.renderItemStack(renderMainHand,  xOffset);
            GlStateManager.popMatrix();
        }
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f,  1500000.0f);
        GlStateManager.popMatrix();
    }

    private void renderItemStack(final ItemStack stack,  final int x) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        Nametags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        Nametags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack,  x,  -30);
        Nametags.mc.getRenderItem().renderItemOverlays(Nametags.mc.fontRenderer,  stack,  x,  -30);
        Nametags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f,  0.5f,  0.5f);
        GlStateManager.disableDepth();
        if (this.mode.getMode() != "none") {
            this.renderEnchantmentText(stack,  x);
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f,  2.0f,  2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(final ItemStack stack,  final int x) {
        int enchantmentY = -34;
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.drawStringWithShadow("gapp",  (float)(x * 2),  (float)enchantmentY,  -3977919);
            enchantmentY -= 8;
        }
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc != null) {
                if (this.mode.getMode() == "minimal") {
                    if (!(enc instanceof EnchantmentProtection)) {
                        continue;
                    }
                    final EnchantmentProtection e = (EnchantmentProtection)enc;
                    if (e.protectionType != EnchantmentProtection.Type.EXPLOSION && e.protectionType != EnchantmentProtection.Type.ALL) {
                        continue;
                    }
                }
                String encName = enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0,  1).toLowerCase()) : enc.getTranslatedName((int)level).substring(0,  1).toLowerCase();
                encName += level;
                this.drawStringWithShadow(encName,  (float)(x * 2),  (float)enchantmentY,  -1);
                enchantmentY -= 8;
            }
        }
        if (DamageUtil.hasDurability(stack)) {
            final int percent = DamageUtil.getRoundedDamage(stack);
            final String color = (percent >= 60) ? "§a" : ((percent >= 25) ? "§e" : "§c");
            this.drawStringWithShadow(color + percent + "%",  (float)(x * 2),  (float)enchantmentY,  -1);
        }
    }

    private float getBiggestArmorTag(final EntityPlayer player) {
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (final ItemStack stack : player.inventory.armorInventory) {
            float encY = 0.0f;
            if (stack != null) {
                final NBTTagList enchants = stack.getEnchantmentTagList();
                for (int index = 0; index < enchants.tagCount(); ++index) {
                    final short id = enchants.getCompoundTagAt(index).getShort("id");
                    final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                    if (enc != null) {
                        encY += 8.0f;
                        arm = true;
                    }
                }
            }
            if (encY <= enchantmentY) {
                continue;
            }
            enchantmentY = encY;
        }
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect()) {
            float encY2 = 0.0f;
            final NBTTagList enchants2 = renderMainHand.getEnchantmentTagList();
            for (int index2 = 0; index2 < enchants2.tagCount(); ++index2) {
                final short id = enchants2.getCompoundTagAt(index2).getShort("id");
                final Enchantment enc2 = Enchantment.getEnchantmentByID((int)id);
                if (enc2 != null) {
                    encY2 += 8.0f;
                    arm = true;
                }
            }
            if (encY2 > enchantmentY) {
                enchantmentY = encY2;
            }
        }
        final ItemStack renderOffHand;
        if ((renderOffHand = player.getHeldItemOffhand().copy()).hasEffect()) {
            float encY2 = 0.0f;
            final NBTTagList enchants2 = renderOffHand.getEnchantmentTagList();
            for (int index = 0; index < enchants2.tagCount(); ++index) {
                final short id2 = enchants2.getCompoundTagAt(index).getShort("id");
                final Enchantment enc = Enchantment.getEnchantmentByID((int)id2);
                if (enc != null) {
                    encY2 += 8.0f;
                    arm = true;
                }
            }
            if (encY2 > enchantmentY) {
                enchantmentY = encY2;
            }
        }
        return (arm ? 0 : 20) + enchantmentY;
    }

    public static final void drawSmoothRect(float left, float top, float right, float bottom, int color) {
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        RenderUtil.drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RenderUtil.drawRect(left * 2.0f - 1.0f, top * 2.0f, left * 2.0f, bottom * 2.0f - 1.0f, color);
        RenderUtil.drawRect(left * 2.0f, top * 2.0f - 1.0f, right * 2.0f, top * 2.0f, color);
        RenderUtil.drawRect(right * 2.0f, top * 2.0f, right * 2.0f + 1.0f, bottom * 2.0f - 1.0f, color);
        RenderUtil.drawRect(left * 2.0f, bottom * 2.0f - 1.0f, right * 2.0f, bottom * 2.0f, color);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }

    private double interpolate(final double previous,  final double current,  final float delta) {
        return previous + (current - previous) * delta;
    }

    private String getDisplayTag(final EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(Nametags.mc.getSession().getUsername())) {
            name = "You";
        }
        String health = " " + (new DecimalFormat("#0").format(EntityUtil.getHealth((Entity)player)));
        if (!this.health.isEnabled()) {
            health = "";
        }
        String pingStr = "";
        if (this.ping.isEnabled()) {
            try {
                final int responseTime = Objects.requireNonNull(Nametags.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                pingStr =  " " + pingStr + responseTime + "ms";
            }
            catch (Exception ex) {}
        }
        String popStr = "";
        if (this.totemPops.isEnabled()) {
            popStr += Main.totemPopManager.getTotemPopString(player);
        }
        String idString = "";
        if (this.entityID.isEnabled()) {
            idString =  " " + idString + "ID: " + player.getEntityId() + "";
        }
        String gameModeStr = "";
        if (this.gamemode.isEnabled()) {
            gameModeStr =  " " + (player.isCreative() ? (gameModeStr + "[C]") : ((player.isSpectator() || player.isInvisible()) ? (gameModeStr + "[I]") : (gameModeStr + "[S]")));
        }
        return name + health + gameModeStr + pingStr + idString + popStr;
    }


    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        mc.fontRenderer.drawString(text, x, y, color, shadow);
    }

    public void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.getTextureManager().bindTexture(skin);
        drawScaledCustomSizeModalRect(width, height, 8.0f, 8.0f, 8, 8, 13, 13, 64.0f, 64.0f);
    }
}