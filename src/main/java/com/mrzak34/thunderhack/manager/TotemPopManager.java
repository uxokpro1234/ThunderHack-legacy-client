package com.mrzak34.thunderhack.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.*;
import java.util.concurrent.*;
import java.util.*;

public class TotemPopManager
{
    private final Set<EntityPlayer> toAnnounce;
    private Map<EntityPlayer,  Integer> poplist;
    private Minecraft mc = Minecraft.getMinecraft();

    public TotemPopManager() {
        this.toAnnounce = new HashSet<EntityPlayer>();
        this.poplist = new ConcurrentHashMap<EntityPlayer,  Integer>();
    }


    public void onTotemPop(final EntityPlayer player) {
        this.popTotem(player);
        if (!player.equals((Object)mc.player)) {
            this.toAnnounce.add(player);
        }
    }

    public void onDeath(final EntityPlayer player) {
        if (this.getTotemPops(player) != 0 && !player.equals((Object)mc.player)) {
            int playerNumber = 0;
            for (final char character : player.getName().toCharArray()) {
                playerNumber += character;
                playerNumber *= 10;
            }
            this.toAnnounce.remove(player);
        }
        this.resetPops(player);
    }

    public void onLogout(final EntityPlayer player,  final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.resetPops(player);
        }
    }

    public void onOwnLogout(final boolean clearOnLogout) {
        if (clearOnLogout) {
            this.clearList();
        }
    }

    public void clearList() {
        this.poplist = new ConcurrentHashMap<EntityPlayer,  Integer>();
    }

    public void resetPops(final EntityPlayer player) {
        this.setTotemPops(player,  0);
    }

    public void popTotem(final EntityPlayer player) {
        this.poplist.merge(player,  1,  Integer::sum);
    }

    public void setTotemPops(final EntityPlayer player,  final int amount) {
        this.poplist.put(player,  amount);
    }

    public int getTotemPops(final EntityPlayer player) {
        final Integer pops = this.poplist.get(player);
        if (pops == null) {
            return 0;
        }
        return pops;
    }

    public String getTotemPopString(final EntityPlayer player) {
        return "§f" + ((this.getTotemPops(player) <= 0) ? "" : ("-" + this.getTotemPops(player) + " "));
    }
}
