package com.soundrecording.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.Screens.Widgets.MP4Button;
import com.soundrecording.Screens.Widgets.MP4TimelineSlider;
import com.soundrecording.Screens.Widgets.MP4VolumeSlider;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MP4PlayerScreen extends HandledScreen<MP4PlayerScreenHandler> {
    public static Identifier GUI_TEXTURE =
            Identifier.of(SoundRecordingMod.MOD_ID, "textures/gui/mp4player_gui.png");

    public static final ButtonTextures RECORDBUTTON_TEXTURE = new ButtonTextures(
        Identifier.of(SoundRecordingMod.MOD_ID, "mp4recordbutton"),
        Identifier.of(SoundRecordingMod.MOD_ID, "mp4recordbutton_selected")
    );

    public static final ButtonTextures STOPRECORDBUTTON_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4stoprecordbutton"),
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4stoprecordbutton_selected")
    );

    public static final ButtonTextures PLAYBUTTON_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4playbutton"),
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4playbutton_selected")
    );

    public static final ButtonTextures STOPBUTTON_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4stopbutton"),
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4stopbutton_selected")
    );

    public static final ButtonTextures SOUNDAROUNDBUTTON_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4soundaroundbutton"),
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4soundaroundbutton_selected")
    );

    public static final ButtonTextures NOSOUNDAROUNDBUTTON_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4nosoundaroundbutton"),
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4nosoundaroundbutton_selected")
    );

    public static final ButtonTextures FFB_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4ffb"),
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4ffb_selected")
    );

    public static final ButtonTextures FBB_TEXTURE = new ButtonTextures(
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4fbb"),
            Identifier.of(SoundRecordingMod.MOD_ID, "mp4fbb_selected")
    );

    MP4TimelineSlider timelineSlider;
    MP4Button recordbutton;
    MP4Button playbutton;
    MP4Button soundaroundbutton;
    TexturedButtonWidget ffb;
    TexturedButtonWidget fbb;

    ItemStack itemStack;


    public MP4PlayerScreen(MP4PlayerScreenHandler handler, PlayerInventory inventory, Text title){
        super(handler, inventory, title);
        this.itemStack = handler.itemStack;
    }

    @Override
    protected void init(){
        super.init();
        this.handler.addListener(new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
            }
            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {}
        });
        buildButtons();
        volumeSliderBuild();
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - backgroundWidth)/2;
        int y = (height - backgroundHeight)/2;

        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        if (this.focusedSlot != null && this.focusedSlot.hasStack()) {
            ItemStack stack = this.focusedSlot.getStack();
            context.drawTooltip(this.textRenderer, this.getTooltipFromItem(stack), mouseX, mouseY);
        }

        String mode;
        if(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()){
            mode = MP4PlayerStatus.fromInt(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus()).name();
            context.drawText(
                    this.textRenderer,
                    Text.literal("Mode: " + mode),
                    x + 85,
                    y + 15,
                    0x373737,
                    false
            );
        }
        else {
            mode = MP4PlayerStatus.fromInt(itemStack.get(ModComponents.STATUS_COMPONENT).playstatus()).name();
            context.drawText(
                    this.textRenderer,
                    Text.literal("Mode: " + mode),
                    x + 85,
                    y + 15,
                    0x373737,
                    false
            );

            if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) return;
            ItemStack sdstack = itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack();
            int size = sdstack.get(ModComponents.RECORDING_COMPONENT).size();
            int length_min = sdstack.get(ModComponents.TICK_COMPONENT).tick()/1200;
            int length_sec = (sdstack.get(ModComponents.TICK_COMPONENT).tick()/20)%60;
            String length = String.format("%02d:%02d", length_min, length_sec);

            int current_min = itemStack.get(ModComponents.TICK_COMPONENT).tick()/1200;
            int current_sec = (itemStack.get(ModComponents.TICK_COMPONENT).tick()/20)%60;
            String current = String.format("%02d:%02d", current_min, current_sec);

            context.drawText(
                    this.textRenderer,
                    Text.literal("Sound Count: " + size),
                    x + 85,
                    y + 30,
                    0x373737,
                    false
            );

            context.drawText(
                    this.textRenderer,
                    Text.literal("Length: " + length),
                    x + 85,
                    y + 45,
                    0x373737,
                    false
            );

            context.drawText(
                    this.textRenderer,
                    Text.literal("Current: " + current),
                    x + 85,
                    y + 60,
                    0x373737,
                    false
            );
        }
    }

    public void updateData(ItemStack stack, int id){
        this.itemStack = stack;
        if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty() || id == 0){
            this.client.execute(() -> {
                buildButtons();
            });
        }
        else {
            this.client.execute(() -> {
                this.timelineSliderBuild();
            });
        }
    }

    public void updateVolume(ItemStack stack){
        this.itemStack = stack;
    }

    void buildButtons(){
        recordButtonBuild();
        playButtonBuild();
        timelineSliderBuild();
        soundaroundButtonBuild();
        FFBBuild();
        FBBBuild();
    }

    void recordButtonBuild(){
        remove(recordbutton);
        int id = (itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.PlayMode.ordinal())? 0: 1;
        recordbutton = new MP4Button(x + 54, y + 55, 13, 13,
                RECORDBUTTON_TEXTURE, STOPRECORDBUTTON_TEXTURE, id, (btn) -> {
            if(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.PlayMode.ordinal()){
                this.client.interactionManager.clickButton(handler.syncId, 1);
                MinecraftClient.getInstance().setScreen(null);
            }
            else if(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()){
                this.client.interactionManager.clickButton(handler.syncId, 0);
                ((MP4Button)btn).switchTexture(0);
            }
        });
        this.addDrawableChild(recordbutton);
    }

    void playButtonBuild(){
        remove(playbutton);
        int id = itemStack.get(ModComponents.STATUS_COMPONENT).playstatus() == MP4PlayerStatus.Idle.ordinal()? 0: 1;
        playbutton = new MP4Button(x + 28, y + 55, 13, 13,
                PLAYBUTTON_TEXTURE, STOPBUTTON_TEXTURE, id, (btn) -> {
            if(itemStack.get(ModComponents.STATUS_COMPONENT).playstatus() == MP4PlayerStatus.Idle.ordinal()){
                this.client.interactionManager.clickButton(handler.syncId, 11);
                ((MP4Button)btn).switchTexture(1);
            }
            else if(itemStack.get(ModComponents.STATUS_COMPONENT).playstatus() == MP4PlayerStatus.Loop.ordinal()){
                this.client.interactionManager.clickButton(handler.syncId, 10);
                ((MP4Button)btn).switchTexture(0);
            }
        });
        this.addDrawableChild(playbutton);
    }

    void soundaroundButtonBuild(){
        remove(soundaroundbutton);
        int id = itemStack.get(ModComponents.IS_SOUNDAROUND_COMPONENT).issoundaround()? 0: 1;
        soundaroundbutton = new MP4Button(x + 67, y + 55, 13, 13,
                SOUNDAROUNDBUTTON_TEXTURE, NOSOUNDAROUNDBUTTON_TEXTURE, id, (btn) -> {
                if(itemStack.get(ModComponents.IS_SOUNDAROUND_COMPONENT).issoundaround()){
                    this.client.interactionManager.clickButton(handler.syncId, 20);
                    ((MP4Button)btn).switchTexture(1);
                }
                else {
                    this.client.interactionManager.clickButton(handler.syncId, 21);
                    ((MP4Button)btn).switchTexture(0);
                }
            });
        this.addDrawableChild(soundaroundbutton);
    }

    void FBBBuild(){
        remove(ffb);
        ffb = new TexturedButtonWidget(x + 15, y + 55, 13, 13,
                FBB_TEXTURE, (btn) -> {
            this.client.interactionManager.clickButton(handler.syncId, 31);
        });
        this.addDrawableChild(ffb);
    }

    void FFBBuild(){
        remove(fbb);
        fbb = new TexturedButtonWidget(x + 41, y + 55, 13, 13,
                FFB_TEXTURE, (btn) -> {
            this.client.interactionManager.clickButton(handler.syncId, 30);
        });
        this.addDrawableChild(fbb);
    }

    void timelineSliderBuild(){
        remove(timelineSlider);
        if(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()) return;
        if(itemStack.isEmpty() || itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) return;
        int tick = itemStack.get(ModComponents.TICK_COMPONENT).tick();
        int maxtick = itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).tick();
        timelineSlider = new MP4TimelineSlider(x + 15, y + 70,144, 12, Text.literal(""),
                 (double) tick/maxtick, itemStack);
        if(timelineSlider != null)
            this.addDrawableChild(timelineSlider);
    }

    void volumeSliderBuild(){
        if(itemStack.isEmpty()) return;
        float volume = itemStack.get(ModComponents.VOLUME_COMPONENT).volume();
        MP4VolumeSlider volumeSlider = new MP4VolumeSlider(x + 68, y + 15,12, 39, Text.literal(""), volume);
        this.addDrawableChild(volumeSlider);
    }

}
