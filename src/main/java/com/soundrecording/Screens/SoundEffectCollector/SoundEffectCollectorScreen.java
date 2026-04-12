package com.soundrecording.Screens.SoundEffectCollector;

import com.mojang.blaze3d.systems.RenderSystem;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SoundEffectCollectorScreen extends HandledScreen<SoundEffectCollectorScreenHandler> {
    public static final Identifier GUI_TEXTURE =
            Identifier.of(SoundRecordingMod.MOD_ID, "textures/gui/soundeffectcollector_gui.png");

    public SoundEffectCollectorScreen(SoundEffectCollectorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth)/2;
        int y = (height - backgroundHeight)/2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }
}
