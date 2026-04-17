package com.soundrecording.Screens.SoundEffectBoard;

import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.ModComponents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Set;

public class SEBScreen extends HandledScreen<SEBScreenHandler> {
    private TextFieldWidget searchBox;
    private SEBListWidget soundList;
    private SEBPitchSlider slider;
    private Set<SoundCodec> soundIdentifiers;
    private List<SoundCodec> filtered;
    public float pitch;

    public SEBScreen(SEBScreenHandler handler, PlayerInventory inventory, Text title){
        super(handler, inventory, title);
        this.soundIdentifiers = this.handler.soundIdentifiers;
        this.pitch = handler.itemStack.get(ModComponents.PITCH_COMPONENT).value();
    }

    @Override
    protected void init() {
        this.searchBox = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, Text.literal("Search"));
        this.searchBox.setChangedListener(this::filterSounds);

        this.addSelectableChild(this.searchBox);
        this.addDrawableChild(this.searchBox);

        this.soundList = new SEBListWidget(this.client, this.width, this.height - 10, 32, 25);
        this.addSelectableChild(this.soundList);

        filterSounds("");
        pitchSliderBuild();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        this.soundList.render(context, mouseX, mouseY, delta);
        this.searchBox.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        this.slider.render(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers) || this.client.options.inventoryKey.matchesKey(keyCode, scanCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.searchBox.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }

    public void updateData(){
        this.soundList.updateEntries(filtered, pitch);
    }

    private void filterSounds(String query) {
        String lowerQuery = query.toLowerCase();
        filtered = soundIdentifiers.stream()
                .filter(id -> id.soundIdentifier().toString().toLowerCase().contains(lowerQuery))
                .toList();

        updateData();
    }

    private void pitchSliderBuild(){
        if(handler.itemStack.isEmpty()) return;
        slider = new SEBPitchSlider(x + 68, y + 15,12, 39, Text.literal(""), pitch/2, this){
            @Override
            protected void applyValue() {
                pitch = (float) this.value*2;
                updateData();
            }
        };
        this.addDrawableChild(slider);
    }
}
