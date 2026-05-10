package com.mrzak34.thunderhack.module.modules.render;

import java.awt.Color;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.module.modules.exploit.PhobosAWP;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockHighlight
        extends Module {
    public NumberSetting lineWidth = new NumberSetting("Line",1f,1f,50f,1f);
    public BooleanSetting outline = new BooleanSetting("Outlie",true);
    public BooleanSetting box = new BooleanSetting("Box",true);
    public NumberSetting boxAlpha = new NumberSetting("BoxAlpha",85,1,255,1);

    public BlockHighlight() {
        super("BlockHighlight", 0, Category.RENDER, false);
        this.addSettings(lineWidth,outline,box,boxAlpha);
    }
    private static BlockHighlight INSTANCE = new BlockHighlight();
    public static BlockHighlight getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockHighlight();
        }
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            Color color = new Color(75, 0, 164, 255);
            RenderUtil.drawBoxESP(blockpos, color, false, color,(float) lineWidth.getValue(), outline.isEnabled(), box.isEnabled(),(int) boxAlpha.getValue(), false);

        }
    }
}

