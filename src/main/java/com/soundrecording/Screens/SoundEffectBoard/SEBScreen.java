package com.soundrecording.Screens.SoundEffectBoard;

import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Screens.StateButton;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;

public class SEBScreen extends HandledScreen<SEBScreenHandler> {
    private TextFieldWidget searchBox;
    private TextFieldWidget pitchBox;
    private SEBListWidget soundList;
    private SEBPitchSlider slider;
    private Set<SoundCodec> soundIdentifiers;
    private List<SoundCodec> filtered;
    private int pitchButtonState;
    private StateButton pitchbutton;
    public float pitch;
    static int RED = 0xFF5555;
    static int WHITE = 0xFFFFFF;
    static int GREY = 0xA0A0A0;

    public static final ButtonTextures NATURALBUTTON_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "sebnaturalbutton"),
            Identifier.of(SoundRecordingMod.MOD_ID, "sebnaturalbutton_selected")
    );

    public static final ButtonTextures SFBUTTON_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "sebsfbutton"),
            Identifier.of(SoundRecordingMod.MOD_ID, "sebsfbutton_selected")
    );

    public SEBScreen(SEBScreenHandler handler, PlayerInventory inventory, Text title){
        super(handler, inventory, title);
        this.soundIdentifiers = this.handler.soundIdentifiers;
        this.pitch = -1;
        this.pitchButtonState = 0;
    }

    @Override
    protected void init() {
        //this.searchBox = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 22, 200, 20, Text.literal("Search"));
        this.searchBox = new TextFieldWidget(this.textRenderer, this.width / 2 + 100, 5, 200, 20, Text.literal("Search"));
        this.searchBox.setPlaceholder(Text.literal("Search").withColor(GREY));
        this.searchBox.setChangedListener(this::filterSounds);
        this.addSelectableChild(this.searchBox);
        this.addDrawableChild(this.searchBox);

        this.pitchBox = new TextFieldWidget(this.textRenderer, this.width / 2 - 210, 5, 100, 20, Text.literal("pitch"));
        this.pitchBox.setPlaceholder(Text.literal("Pitch (0.0 - 2.0)").withColor(GREY));
        this.pitchBox.setTextPredicate(s -> s.matches("^[0-9]{0,1}(\\.[0-9]{0,6})?$"));
        this.pitchBox.setChangedListener(this::setPitch);
        this.addSelectableChild(this.pitchBox);
        this.addDrawableChild(this.pitchBox);

        this.soundList = new SEBListWidget(this.client, this.width, this.height - 10, 32, 25);
        this.addSelectableChild(this.soundList);

        pitchButtonBuild();
//        pitchSliderBuild();

        filterSounds("");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        this.soundList.render(context, mouseX, mouseY, delta);
        this.searchBox.render(context, mouseX, mouseY, delta);
        this.pitchbutton.render(context, mouseX, mouseY, delta);
//        this.slider.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, WHITE);
        if(pitchButtonState != 1) return;
        context.drawText(this.textRenderer, Text.literal("Pitch: " + this.pitch), this.width / 2 - 296, 10, WHITE, false);
        this.pitchBox.render(context, mouseX, mouseY, delta);
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

    void pitchButtonBuild(){
        int id = pitchButtonState;
        pitchbutton = new StateButton(this.width / 2 - 100, 5, 20, 20,
                NATURALBUTTON_TEXTURE, SFBUTTON_TEXTURE, id, (btn) -> {
            pitchSwitch(pitchButtonState);
            ((StateButton) btn).switchTexture(pitchButtonState);
        });
        this.addDrawableChild(pitchbutton);
    }

    void pitchSwitch(int pitchButtonState){
        this.pitchButtonState = (pitchButtonState == 0)? 1: 0;
        if(this.pitchButtonState == 0){
            pitchBox.active = false;
            pitch = -1;
        }
        else {
            pitchBox.active = true;
            if(pitchBox.getText().isEmpty()){
                pitch = 1;
            }
            else{
                setPitch(pitchBox.getText());
            }
        }
        updateData();
    }

    private void setPitch(String value){
        if(value.isEmpty() || value.equals(".")) return;
        float pitch = Float.parseFloat(value);
        this.pitchBox.setEditableColor((pitch >= 0 && pitch <= 2.0) ? WHITE : RED);
        if((pitch < 0 || pitch > 2.0)) return;
        this.pitch = pitch;
        updateData();
    }
}
