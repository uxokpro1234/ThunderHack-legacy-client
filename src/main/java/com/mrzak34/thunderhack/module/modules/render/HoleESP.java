package com.mrzak34.thunderhack.module.modules.render;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.BlockUtil;
import com.mrzak34.thunderhack.util.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class HoleESP
        extends Module {
    private static HoleESP INSTANCE = new HoleESP();
    public NumberSetting range = new NumberSetting("RangeX", 0, 1, 10, 1);
    public NumberSetting rangeY = new NumberSetting("RangeY", 0, 1, 10, 1);
    public NumberSetting red = new NumberSetting("Red", 0, 1, 10, 1);
    public NumberSetting green = new NumberSetting("Green", 0, 1, 10, 1);
    public NumberSetting blue = new NumberSetting("Blue", 0, 1, 10, 1);
    public NumberSetting alpha = new NumberSetting("Alpha", 60, 1, 255, 1);
    public NumberSetting boxAlpha = new NumberSetting("BoxAlpha", 60, 1, 255, 1);
    public NumberSetting lineWidth = new NumberSetting("BoxAlpha", 10.0f, 1.0f, 50.0f, 1.0f);
    public NumberSetting safeRed = new NumberSetting("BedrockRed", 10.0f, 1.0f, 50.0f, 1.0f);
    public NumberSetting safeGreen = new NumberSetting("BedrockGreen", 10.0f, 1.0f, 50.0f, 1.0f);
    public NumberSetting safeBlue = new NumberSetting("BedrockBlue", 10.0f, 1.0f, 50.0f, 1.0f);
    public NumberSetting safeAlpha = new NumberSetting("BedrockAlpha", 10.0f, 1.0f, 50.0f, 1.0f);

    public BooleanSetting future = new BooleanSetting("FutureRender", true);
    public BooleanSetting fov = new BooleanSetting("FutureRender", true);
    public BooleanSetting renderOwn = new BooleanSetting("FutureRender", true);
    public BooleanSetting box = new BooleanSetting("FutureRender", true);
    public BooleanSetting outline = new BooleanSetting("FutureRender", true);

    public NumberSetting cRed = new NumberSetting("OL-Red", 0, 1, 255, 1);
    public NumberSetting cGreen = new NumberSetting("OL-Green", 0, 1, 255, 1);
    public NumberSetting cBlue = new NumberSetting("OL-Blue", 255, 1, 255, 1);
    public NumberSetting cAlpha = new NumberSetting("OL-Alpha", 255, 1, 255, 1);
    public NumberSetting safecRed = new NumberSetting("OL-BedrockRed", 0, 1, 255, 1);
    public NumberSetting safecGreen = new NumberSetting("OL-BedrockGreen", 255, 1, 255, 1);
    public NumberSetting safecBlue = new NumberSetting("OL-BedrockBlue", 0, 1, 255, 1);
    public NumberSetting safecAlpha = new NumberSetting("OL-BedrockAlpha", 255, 1, 255, 1);


    public HoleESP() {
        super("HoleESP", 0, Category.RENDER, false);
        this.setInstance();
        this.addSettings(range,rangeY,green,blue,alpha,boxAlpha, lineWidth, safeRed,safeGreen,safeBlue,safeAlpha,
                future,fov,renderOwn,box, outline,cRed,cGreen,cBlue,cAlpha,safecRed,safecGreen,safecBlue,safecAlpha);
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        assert (HoleESP.mc.renderViewEntity != null);
        Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = (int) (playerPos.getX() - this.range.getValue()); x < playerPos.getX() + this.range.getValue(); ++x) {
            for (int z = (int) (playerPos.getZ() - this.range.getValue()); z < playerPos.getZ() + this.range.getValue(); ++z) {
                for (int y = (int) (playerPos.getY() + this.rangeY.getValue()); y > playerPos.getY() - this.rangeY.getValue(); --y) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) || pos.equals(new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !this.renderOwn.isEnabled() || !BlockUtil.isPosInFov(pos).booleanValue() && this.fov.isEnabled())
                        continue;
                    if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                        RenderUtil.drawBoxESP(this.future.isEnabled() != false ? pos.down() : pos, new Color((int) this.safeRed.getValue(), (int) this.safeGreen.getValue(), (int) this.safeBlue.getValue(), (int) this.safeAlpha.getValue()), this.outline.isEnabled(), new Color((int) this.safecRed.getValue(), (int) this.safecGreen.getValue(), (int) this.safecBlue.getValue(), (int) this.safecAlpha.getValue()), (float) this.lineWidth.getValue(), this.outline.isEnabled(), this.box.isEnabled(),(int) this.boxAlpha.getValue(), true);
                        continue;
                    }
                    if (!BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock()))
                        continue;
                    RenderUtil.drawBoxESP(this.future.isEnabled() != false ? pos.down() : pos, new Color((int) this.red.getValue(), (int) this.green.getValue(), (int) this.blue.getValue(), (int) this.alpha.getValue()), this.outline.isEnabled(), new Color((int) this.cRed.getValue(), (int) this.cGreen.getValue(), (int) this.cBlue.getValue(), (int) this.cAlpha.getValue()), (float) this.lineWidth.getValue(), this.outline.isEnabled(), this.box.isEnabled(),(int) this.boxAlpha.getValue(), true);
                }
            }
        }
    }
}

