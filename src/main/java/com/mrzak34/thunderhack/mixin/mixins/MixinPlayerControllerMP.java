package com.mrzak34.thunderhack.mixin.mixins;

import com.mrzak34.thunderhack.event.events.EventPlayerDamageBlock;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(value = {PlayerControllerMP.class})
public class MixinPlayerControllerMP {
        @Inject(method = "onPlayerDamageBlock", at = {@At("HEAD")}, cancellable = true)
        public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> p_Info) {
            EventPlayerDamageBlock event = new EventPlayerDamageBlock(posBlock, directionFacing);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                p_Info.setReturnValue(false);
                p_Info.cancel();
            }
        }

    }


