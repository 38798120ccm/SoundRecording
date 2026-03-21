package com.soundrecording.Screens.SoundEffectBook;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Items.MP4Player.MP4PlayerInventory;
import com.soundrecording.Items.MP4Player.MP4PlayerSlot;
import com.soundrecording.Screens.ModScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class SoundEffectBookScreenHandler extends ScreenHandler {

    public ItemStack itemStack;
    public final PlayerInventory playerInventory;
    private final MP4PlayerInventory mp4PlayerInventory;

    public SoundEffectBookScreenHandler(int synvId, PlayerInventory playerInventory, ItemStackCodec payload){
        this(synvId, playerInventory, payload.itemStack());
    }

    public SoundEffectBookScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ModScreenHandler.SOUND_EFFECT_BOOK_SCREEN_HANDLER_SCREEN_HANDLER, syncId);
        this.itemStack = itemStack;
        this.mp4PlayerInventory = new MP4PlayerInventory(this, itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack());
        this.playerInventory = playerInventory;

        this.addSlot(new MP4PlayerSlot(mp4PlayerInventory, 0, 33, 33, itemStack));
//
//        addPlayerInventory(playerInventory);
//        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }
}
