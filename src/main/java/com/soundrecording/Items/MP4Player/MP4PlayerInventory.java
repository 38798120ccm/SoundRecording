package com.soundrecording.Items.MP4Player;

import com.soundrecording.Items.ModItems;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class MP4PlayerInventory extends SimpleInventory {
    private final ScreenHandler handler;

    public MP4PlayerInventory(ScreenHandler handler, ItemStack item){
        super(1);
        addStack(item);
        this.handler = handler;
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isOf(ModItems.MICROSD);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if(handler != null){
            handler.onContentChanged(this);
        }
    }
}
