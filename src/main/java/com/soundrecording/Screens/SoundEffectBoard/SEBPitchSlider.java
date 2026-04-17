package com.soundrecording.Screens.SoundEffectBoard;

import com.soundrecording.Payload.PitchSliderC2SPayload;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SEBPitchSlider extends SliderWidget {
    SEBScreen screen;
    private static final Identifier SLIDER_TEXTURE = Identifier.of(SoundRecordingMod.MOD_ID, "textures/gui/mp4player/mp4volume_slider.png");
    private static final Identifier HANDLE_TEXTURE = Identifier.of(SoundRecordingMod.MOD_ID, "textures/gui/mp4player/mp4volume_handler.png");

    public SEBPitchSlider(int x, int y, int width, int height, Text text, double value, Screen screen) {
        super(x, y, width, height, text, value);
        this.screen = (SEBScreen) screen;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(SLIDER_TEXTURE, this.getX(), this.getY(), 0, 0, this.width, this.height, 12, 39);

        int handleY = this.getY() + (int)(((1 - this.value) * (double)(this.height - 8)));
        context.drawTexture(HANDLE_TEXTURE, this.getX(), handleY,  0, 0, this.width, 8, 12, 8);
    }

    @Override
    protected void updateMessage() {

    }

    @Override
    protected void applyValue() {

    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setValueFromMouse(mouseY);
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        setValueFromMouse(mouseY);
        sendPayload();
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        sendPayload();
    }

    private void setValueFromMouse(double mouseY){
        this.setValue(1 - (mouseY - (double)(this.getY() + 4)) / (double)(this.height - 8));
    }

    private void setValue(double value) {
        double d = this.value;
        this.value = MathHelper.clamp(value, 0.0, 1.0);
        if (d != this.value) {
            this.applyValue();
        }

        this.updateMessage();
    }

    private void sendPayload(){
        ClientPlayNetworking.send(new PitchSliderC2SPayload((float) value*2));
    }
}
