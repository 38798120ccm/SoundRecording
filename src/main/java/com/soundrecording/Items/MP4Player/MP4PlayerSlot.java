package com.soundrecording.Items.MP4Player;

import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Items.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class MP4PlayerSlot extends Slot { ;

    ItemStack mp4stack;

    public MP4PlayerSlot(Inventory inventory, int index, int x, int y, ItemStack mp4stack) {
        super(inventory, index, x, y);
        this.mp4stack = mp4stack;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(ModItems.MICROSD);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        if(mp4stack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()) return false;
        return super.canTakeItems(playerEntity);
    }
}
