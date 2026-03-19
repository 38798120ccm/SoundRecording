package com.soundrecording.mixin;

import net.minecraft.client.sound.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {

    @ModifyConstant(method = "init", constant = @Constant(intValue = 8), remap = true)
    private int increaseSources(int original) {
        return 64;
    }
}
