package com.mrzak34.thunderhack.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.module.modules.exploit.PhobosAWP;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PortalESP
        extends Module {
    private int cooldownTicks;
    private static Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<BlockPos> blockPosArrayList = new ArrayList();
    public NumberSetting distance = new NumberSetting("Distance",60,10,120,1);
    public NumberSetting lineWidth = new NumberSetting("Line",1f,1f,50f,1f);
    public BooleanSetting outline = new BooleanSetting("Outlie",true);
    public BooleanSetting box = new BooleanSetting("Box",true);
    public NumberSetting boxAlpha = new NumberSetting("BoxAlpha",85,1,255,1);

    public PortalESP() {
        super("PortalESP", 0, Category.RENDER, false);
        this.addSettings(distance,lineWidth,outline,box,boxAlpha);
    }
    private static PortalESP INSTANCE = new PortalESP();
    public static PortalESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PortalESP();
        }
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (PortalESP.mc.world == null) {
            return;
        }
        if (this.cooldownTicks < 1) {
            this.blockPosArrayList.clear();
            this.compileDL();
            this.cooldownTicks = 80;
        }
        --this.cooldownTicks;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (PortalESP.mc.world == null) {
            return;
        }
        for (BlockPos pos : this.blockPosArrayList) {
            RenderUtil.drawBoxESP(pos, new Color(204, 0, 153, 255), false, new Color(204, 0, 153, 255), (float) lineWidth.getValue(), outline.isEnabled(), box.isEnabled(), (int) boxAlpha.getValue(), false);
        }
    }

    private void compileDL() {
        if (PortalESP.mc.world == null || PortalESP.mc.player == null) {
            return;
        }
        for (int x = (int) ((int)PortalESP.mc.player.posX - this.distance.getValue()); x <= (int)PortalESP.mc.player.posX + this.distance.getValue(); ++x) {
            for (int y = (int) ((int) PortalESP.mc.player.posY - this.distance.getValue()); y <= (int)PortalESP.mc.player.posY + this.distance.getValue(); ++y) {
                int z = (int)Math.max(PortalESP.mc.player.posZ - (double)this.distance.getValue(), 0.0);
                while ((double)z <= Math.min(PortalESP.mc.player.posZ + (double)this.distance.getValue(), 255.0)) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = PortalESP.mc.world.getBlockState(pos).getBlock();
                    if (block instanceof BlockPortal) {
                        this.blockPosArrayList.add(pos);
                    }
                    ++z;
                }
            }
        }
    }
}
