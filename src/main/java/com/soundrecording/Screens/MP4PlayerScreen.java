package com.soundrecording.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.soundrecording.Screens.Widgets.MP4VolumeSlider;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MP4PlayerScreen extends HandledScreen<MP4PlayerScreenHandler> {
    public static Identifier GUI_TEXTURE =
            Identifier.of(SoundRecordingMod.MOD_ID, "textures/gui/mp4player_gui.png");

    public static final ButtonTextures RECORDBUTTON_TEXTURE = new ButtonTextures(
        Identifier.of(SoundRecordingMod.MOD_ID, "mp4recordbutton"),
        Identifier.of(SoundRecordingMod.MOD_ID, "mp4recordbutton_selected")
    );

    public MP4PlayerScreen(MP4PlayerScreenHandler handler, PlayerInventory inventory, Text title){
        super(handler, inventory, title);
    }

    @Override
    protected void init(){
        super.init();
        recordButtonBuild();
        isDirectionalButtonBuild();
        volumeButtonBuild();

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

    void recordButtonBuild(){
        TexturedButtonWidget buttonWidget = new TexturedButtonWidget(x + 55, y + 55, 13, 13, RECORDBUTTON_TEXTURE,
                (btn) -> {
            this.client.interactionManager.clickButton(handler.syncId, 0);
            MinecraftClient.getInstance().setScreen(null);
        });

        this.addDrawableChild(buttonWidget);
    }

    void isDirectionalButtonBuild(){
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("isDirection"), (btn) -> {
            this.client.interactionManager.clickButton(handler.syncId, 1);
        }).dimensions(40, 80, 120, 20).build();
        this.addDrawableChild(buttonWidget);
    }

    void volumeButtonBuild(){
        MP4VolumeSlider slider = new MP4VolumeSlider(this.width / 2 - 100, 120,200, 20, Text.literal("volume"), 1f);
        this.addDrawableChild(slider);
    }
}
