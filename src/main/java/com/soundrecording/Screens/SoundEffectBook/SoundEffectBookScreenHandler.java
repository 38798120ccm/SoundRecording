package com.soundrecording.Screens.SoundEffectBook;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Screens.ModScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;

public class SoundEffectBookScreenHandler extends ScreenHandler {
    public ItemStack itemStack;
    public final PlayerInventory playerInventory;
    public Set<SoundCodec> soundIdentifiers;

    public SoundEffectBookScreenHandler(int synvId, PlayerInventory playerInventory, ItemStackCodec payload){
        this(synvId, playerInventory, payload.itemStack());
    }

    public SoundEffectBookScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ModScreenHandler.SOUND_EFFECT_BOOK_SCREEN_HANDLER_SCREEN_HANDLER, syncId);
        this.itemStack = itemStack;
        this.playerInventory = playerInventory;
        this.soundIdentifiers = itemStack.get(ModComponents.SOUNDLIST_COMPONENT).soundIdentifiers();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }


}
