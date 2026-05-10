package com.mrzak34.thunderhack.module.modules.movement;

import com.mrzak34.thunderhack.module.Category;
import com.mrzak34.thunderhack.module.Module;
import com.mrzak34.thunderhack.settings.BooleanSetting;
import com.mrzak34.thunderhack.settings.ModeSetting;
import com.mrzak34.thunderhack.settings.NumberSetting;
import com.mrzak34.thunderhack.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import org.lwjgl.input.Keyboard;

public class OnGround extends Module {
    private Minecraft mc = Minecraft.getMinecraft();
    public ModeSetting mode = new ModeSetting("Mode","ONGROUND", "ONGROUND","NOGROUND");

    public NumberSetting speed = new NumberSetting("Speed",13, 1, 20, 1);
    public NumberSetting jump = new NumberSetting("Jump",1, 1, 10, 1);
    public NumberSetting inc = new NumberSetting("Incertment",1000, 1, 1000, 1);
    public BooleanSetting motion = new BooleanSetting("Motion",false);

    public OnGround() {

        super("OnGround",0, Category.MOVEMENT,false);
        this.setKey(Keyboard.KEY_NONE);
        this.addSettings(mode,speed,jump,inc,motion);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if(motion.isEnabled()){
            mc.player.motionY = -1;
        }
    }


    @SubscribeEvent
    public void onUpdateInput(final InputUpdateEvent event){



        if(mode.getMode() == "ONGROUND"){
        if(mc.player.onGround && EntityUtil.isMoving()){
            float spid = (float) speed.getValue() / 10;
            float jamp = (float) ((float) jump.getValue() / inc.getValue());
            mc.player.motionX *= spid;
            mc.player.motionZ *= spid;
            mc.player.motionY = jamp;

        }}


        if(mode.getMode() == "NOGROUND"){
            if(mc.player.onGround && EntityUtil.isMoving()) {
                mc.player.jump();
                float spid = (float) speed.getValue() / 10;
                mc.player.motionX = -spid;
                mc.player.motionZ = -spid;
            }
        }

    }
}