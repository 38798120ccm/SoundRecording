package com.soundrecording.Screens.Widgets;

import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Payload.TimelineSliderC2SPayload;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MP4TimelineSlider extends SliderWidget {
    private static final Identifier SLIDER_TEXTURE = Identifier.of(SoundRecordingMod.MOD_ID, "textures/gui/mp4player/mp4timeline_slider.png");
    private static final Identifier HANDLE_TEXTURE = Identifier.of(SoundRecordingMod.MOD_ID, "textures/gui/mp4player/mp4timeline_handler.png");
    ItemStack stack;
    int prestatus;
    private long lastUpdateTime = 0;
    private long delay = 200;

    public MP4TimelineSlider(int x, int y, int width, int height, Text text, double value, ItemStack stack) {
        super(x, y, width, height, text, value);
        this.stack = stack;
        prestatus = stack.get(ModComponents.STATUS_COMPONENT).playstatus();
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(SLIDER_TEXTURE, this.getX(), this.getY(), 0, 0, this.width, this.height, 124, 12);

        int handleX = this.getX() + (int)(this.value * (double)(this.width - 8));
        context.drawTexture(HANDLE_TEXTURE, handleX, this.getY(), 0, 0, 8, this.height, 8, 12);
    }

    @Override
    protected void updateMessage() {

    }

    @Override
    protected void applyValue() {
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        prestatus = stack.get(ModComponents.STATUS_COMPONENT).playstatus();
        ClientPlayNetworking.send(new TimelineSliderC2SPayload((float) value, prestatus, 0));
    }

    @Override
    public void onRelease(double mouseX, double mouseY){
        super.onRelease(mouseX, mouseY);
        ClientPlayNetworking.send(new TimelineSliderC2SPayload((float) this.value, prestatus, 1));
    }

}
