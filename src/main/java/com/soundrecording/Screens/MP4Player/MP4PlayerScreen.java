package com.soundrecording.Screens.MP4Player;

import com.mojang.blaze3d.systems.RenderSystem;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.Items.ModItems;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

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
                itemStack = ((MP4PlayerScreenHandler) handler).itemStack;
                buildButtons();
            }
            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {}
        });
        buildButtons();
        volumeSliderBuild(itemStack);
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
        int duration_hr;
        int duration_min;
        int duration_sec;
        ItemStack sdstack = itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack();
        if(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()){
            duration_hr = itemStack.get(ModComponents.TICK_COMPONENT).tick()/72000;
            duration_min = itemStack.get(ModComponents.TICK_COMPONENT).tick()/1200;
            duration_sec = (itemStack.get(ModComponents.TICK_COMPONENT).tick()/20)%60;

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
            duration_hr =  sdstack.get(ModComponents.TICK_COMPONENT).tick()/72000;
            duration_min = sdstack.get(ModComponents.TICK_COMPONENT).tick()/1200;
            duration_sec = (sdstack.get(ModComponents.TICK_COMPONENT).tick()/20)%60;

            int current_hr =  itemStack.get(ModComponents.TICK_COMPONENT).tick()/72000;
            int current_min = itemStack.get(ModComponents.TICK_COMPONENT).tick()/1200;
            int current_sec = (itemStack.get(ModComponents.TICK_COMPONENT).tick()/20)%60;
            String current = String.format("%02d:%02d:%02d", current_hr, current_min, current_sec);

            context.drawText(
                    this.textRenderer,
                    Text.literal("Current: " + current),
                    x + 85,
                    y + 60,
                    0x373737,
                    false
            );
        }
        if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) return;
        int size = sdstack.get(ModComponents.RECORDING_COMPONENT).size();

        String duration = String.format("%02d:%02d:%02d", duration_hr, duration_min, duration_sec);

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
                Text.literal("Duration: " + duration),
                x + 85,
                y + 45,
                0x373737,
                false
        );
    }

    @Override
    protected void handledScreenTick() {
        ClientPlayerEntity player = this.client.player;
        if(player == null) return;
        ItemStack stack = player.getMainHandStack();
        if(stack.isOf(ModItems.MP4PLAYER)){
            updateDataTick(stack);
        }
    }

    public void updateDataTick(ItemStack stack){
        if(stack.get(ModComponents.STATUS_COMPONENT).recordstatus() != itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus()){
            recordButtonBuild(stack);
            timelineSliderBuild(stack);
        }
        if(stack.get(ModComponents.STATUS_COMPONENT).playstatus() != itemStack.get(ModComponents.STATUS_COMPONENT).playstatus()){
            playButtonBuild(stack);
        }
        if(stack.get(ModComponents.IS_SOUNDAROUND_COMPONENT).issoundaround() != itemStack.get(ModComponents.IS_SOUNDAROUND_COMPONENT).issoundaround()){
            soundaroundButtonBuild(stack);
        }
        this.itemStack = stack;
    }

    public void updateData(ItemStack stack){
        if(timelineSlider != null) {
            timelineSlider.adjustValue(stack);
        }
    }

    void buildButtons(){
        recordButtonBuild(itemStack);
        playButtonBuild(itemStack);
        timelineSliderBuild(itemStack);
        soundaroundButtonBuild(itemStack);
        FFBBuild();
        FBBBuild();
    }

    void recordButtonBuild(ItemStack stack){
        remove(recordbutton);
        int id = (stack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.PlayMode.ordinal())? 0: 1;
        recordbutton = new MP4Button(x + 54, y + 55, 13, 13,
                RECORDBUTTON_TEXTURE, STOPRECORDBUTTON_TEXTURE, id, (btn) -> {
            if(stack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.PlayMode.ordinal()){
                this.client.interactionManager.clickButton(handler.syncId, 1);
                MinecraftClient.getInstance().setScreen(null);
            }
            else if(stack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()){
                this.client.interactionManager.clickButton(handler.syncId, 0);
                ((MP4Button)btn).switchTexture(0);
            }
        });
        this.addDrawableChild(recordbutton);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() != MP4PlayerStatus.Recording.ordinal()){
            if(keyCode == GLFW.GLFW_KEY_SPACE){
                playButtonFunction(itemStack);
                System.out.println("space");
                return true;
            }
            else if(keyCode == GLFW.GLFW_KEY_LEFT){
                this.client.interactionManager.clickButton(handler.syncId, 31);
                return true;
            }
            else if(keyCode == GLFW.GLFW_KEY_RIGHT){
                this.client.interactionManager.clickButton(handler.syncId, 30);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    void playButtonBuild(ItemStack stack){
        remove(playbutton);
        int id = stack.get(ModComponents.STATUS_COMPONENT).playstatus() == MP4PlayerStatus.Idle.ordinal()? 0: 1;
        playbutton = new MP4Button(x + 28, y + 55, 13, 13,
                PLAYBUTTON_TEXTURE, STOPBUTTON_TEXTURE, id, (btn) -> {
            playButtonFunction(stack);
        });
        this.addDrawableChild(playbutton);
    }

    void playButtonFunction(ItemStack stack){
        if(stack.get(ModComponents.STATUS_COMPONENT).playstatus() == MP4PlayerStatus.Idle.ordinal()){
            this.client.interactionManager.clickButton(handler.syncId, 11);
        }
        else if(stack.get(ModComponents.STATUS_COMPONENT).playstatus() == MP4PlayerStatus.Loop.ordinal()){
            this.client.interactionManager.clickButton(handler.syncId, 10);
        }
    }

    void soundaroundButtonBuild(ItemStack stack){
        remove(soundaroundbutton);
        int id = stack.get(ModComponents.IS_SOUNDAROUND_COMPONENT).issoundaround()? 0: 1;
        soundaroundbutton = new MP4Button(x + 67, y + 55, 13, 13,
                SOUNDAROUNDBUTTON_TEXTURE, NOSOUNDAROUNDBUTTON_TEXTURE, id, (btn) -> {
                if(stack.get(ModComponents.IS_SOUNDAROUND_COMPONENT).issoundaround()){
                    this.client.interactionManager.clickButton(handler.syncId, 20);
                }
                else {
                    this.client.interactionManager.clickButton(handler.syncId, 21);
                }
            });
        this.addDrawableChild(soundaroundbutton);
    }

    void FFBBuild(){
        remove(fbb);
        fbb = new TexturedButtonWidget(x + 41, y + 55, 13, 13,
                FFB_TEXTURE, (btn) -> {
            this.client.interactionManager.clickButton(handler.syncId, 40);
            FFBBuild();
        });
        this.addDrawableChild(fbb);
    }

    void FBBBuild(){
        remove(ffb);
        ffb = new TexturedButtonWidget(x + 15, y + 55, 13, 13,
                FBB_TEXTURE, (btn) -> {
            this.client.interactionManager.clickButton(handler.syncId, 41);
            FBBBuild();
        });
        this.addDrawableChild(ffb);
    }

    void timelineSliderBuild(ItemStack stack){
        if(timelineSlider != null){
            if(timelineSlider.isDragging()) return;
            remove(timelineSlider);
        }
        if(stack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()) return;
        if(stack.isEmpty() || stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) return;
        double tick = stack.get(ModComponents.TICK_COMPONENT).tick();
        double maxtick = stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).tick();
        timelineSlider = new MP4TimelineSlider(x + 15, y + 70,144, 12, Text.literal(""),
                 tick/maxtick, stack);
        this.addDrawableChild(timelineSlider);
    }

    void volumeSliderBuild(ItemStack stack){
        if(stack.isEmpty()) return;
        float volume = stack.get(ModComponents.VOLUME_COMPONENT).volume();
        MP4VolumeSlider volumeSlider = new MP4VolumeSlider(x + 68, y + 15,12, 39, Text.literal(""), volume);
        this.addDrawableChild(volumeSlider);
    }

}
