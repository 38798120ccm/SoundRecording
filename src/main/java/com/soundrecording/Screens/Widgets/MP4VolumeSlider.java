package com.soundrecording.Screens.Widgets;

import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Componets.VolumeComponent;
import com.soundrecording.Payload.VolumeSliderC2SPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class MP4VolumeSlider extends SliderWidget {
    public MP4VolumeSlider(int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.literal("" + (int)(this.value * 100)));
    }

    @Override
    protected void applyValue() {
        float finalValue = (float) this.value;

        ClientPlayNetworking.send(new VolumeSliderC2SPayload(finalValue));
    }

    @Override
    public void onRelease(double mouseX, double mouseY){

        super.onRelease(mouseX, mouseY);
    }

}
