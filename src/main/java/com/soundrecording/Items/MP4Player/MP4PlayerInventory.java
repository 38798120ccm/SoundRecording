package com.soundrecording.Items.MP4Player;

import com.soundrecording.Items.ModItems;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

import java.util.Iterator;

public class MP4PlayerInventory extends SimpleInventory {
    private ScreenHandler handler;

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

    //    @Override
//    public boolean canInsert(ItemStack stack){
//        boolean bl = false;
//        Iterator var3 = this.heldStacks.iterator();
//        if (!stack.isOf(ModItems.MICROSD)) { return false;}
//        while(var3.hasNext()) {
//            ItemStack itemStack = (ItemStack)var3.next();
//            if (itemStack.isEmpty() || ItemStack.areItemsAndComponentsEqual(itemStack, stack) && itemStack.getCount() < itemStack.getMaxCount()) {
//                bl = true;
//                break;
//            }
//        }
//
//        return bl;
//    }

    @Override
    public void markDirty() {
        super.markDirty();
        if(handler != null){
            handler.onContentChanged(this);
        }
    }
}
