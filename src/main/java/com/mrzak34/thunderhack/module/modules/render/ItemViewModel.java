package com.mrzak34.thunderhack.module.modules.render;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import org.lwjgl.opengl.GL11;

public class ItemViewModel extends Module {
    private Minecraft mc = Minecraft.getMinecraft();
    public NumberSetting posx = new NumberSetting("posx",1000, -2000, 2000, 1);
    public NumberSetting posy = new NumberSetting("posx",1000, -2000, 2000, 1);
    public NumberSetting posz = new NumberSetting("posx",1000, -2000, 2000, 1);
    float x = (float) posx.getValue() / 1000;
    float y = (float) posx.getValue() / 1000;
    float z = (float) posx.getValue() / 1000;


    public ItemViewModel() {

        super("ItemViewModel",0, Category.RENDER,false);
        this.addSettings(posx,posz,posy);
    }
    private static ItemViewModel INSTANCE = new ItemViewModel();
    public static ItemViewModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ItemViewModel();
        }
        return INSTANCE;
    }
    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void OnArms(RenderSpecificHandEvent event){
        GL11.glTranslatef(x, y, z);
    }
}
