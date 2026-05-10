package com.mrzak34.thunderhack.mixin.mixins;

import com.mrzak34.thunderhack.module.modules.render.Nametags;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderPlayer.class })
public abstract class MixinRenderPlayer
{
    @Shadow public abstract ModelPlayer getMainModel();

    @Inject(method = { "renderEntityName" },  at = { @At("HEAD") },  cancellable = true)

    public void renderEntityNameHook(final AbstractClientPlayer entityIn,  final double x,  final double y,  final double z,  final String name,  final double distanceSq,  final CallbackInfo info) {
        info.cancel();
    }
}
