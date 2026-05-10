package com.mrzak34.thunderhack.manager;

import com.mrzak34.thunderhack.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;

public class ServerManager {
    private final float[] tpsCounts = new float[10];
    private final DecimalFormat format = new DecimalFormat("##.00#");
    private final Timer timer = new Timer();
    private float TPS = 20.0f;
    private long lastUpdate = -1L;
    private String serverBrand = "";
    private static Minecraft mc = Minecraft.getMinecraft();
    public void onPacketReceived() {
        this.timer.reset();
    }
    @SubscribeEvent
    public long serverRespondingTime() {
        return this.timer.getPassedTimeMs();
    }
@SubscribeEvent
    public void update() {
        float tps;
        long currentTime = System.currentTimeMillis();
        if (this.lastUpdate == -1L) {
            this.lastUpdate = currentTime;
            return;
        }
        long timeDiff = currentTime - this.lastUpdate;
        float tickTime = (float) timeDiff / 20.0f;
        if (tickTime == 0.0f) {
            tickTime = 50.0f;
        }
        if ((tps = 1000.0f / tickTime) > 20.0f) {
            tps = 20.0f;
        }
        System.arraycopy(this.tpsCounts, 0, this.tpsCounts, 1, this.tpsCounts.length - 1);
        this.tpsCounts[0] = tps;
        double total = 0.0;
        for (float f : this.tpsCounts) {
            total += f;
        }
        if ((total /= this.tpsCounts.length) > 20.0) {
            total = 20.0;
        }
        this.TPS = Float.parseFloat(this.format.format(total));
        this.lastUpdate = currentTime;
    }

    @SubscribeEvent
    public void reset() {
        Arrays.fill(this.tpsCounts, 20.0f);
        this.TPS = 20.0f;
    }
    @SubscribeEvent
    public float getTpsFactor() {
        return 20.0f / this.TPS;
    }
    @SubscribeEvent
    public float getTPS() {
        return this.TPS;
    }
    @SubscribeEvent
    public String getServerBrand() {
        return this.serverBrand;
    }

    public void setServerBrand(String brand) {
        this.serverBrand = brand;
    }
@SubscribeEvent
    public int getPing() {
        if (ServerManager.fullNullCheck()) {
            return 0;
        }
        try {
            return Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
        } catch (Exception e) {
            return 0;
        }
    }

    private static boolean fullNullCheck(){
        return false;
    }

}

