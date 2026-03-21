package com.soundrecording.Screens.SoundEffectCollector;

import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Items.MP4Player.MP4PlayerInventory;
import com.soundrecording.Items.MP4Player.MP4PlayerSlot;
import com.soundrecording.Screens.ModScreenHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class SoundEffectCollectorScreenHandler extends ScreenHandler {

    protected SoundEffectCollectorScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
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
