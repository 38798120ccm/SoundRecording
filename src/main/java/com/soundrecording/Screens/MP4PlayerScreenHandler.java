package com.soundrecording.Screens;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Items.MP4Player.MP4PlayerInventory;
import com.soundrecording.Items.MP4Player.MP4PlayerSlot;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.Items.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class MP4PlayerScreenHandler extends ScreenHandler {
    private final ItemStack itemStack;
    private final PlayerInventory playerInventory;
    private final MP4PlayerInventory mp4PlayerInventory;

    // Client Constructor
    public MP4PlayerScreenHandler(int synvId, PlayerInventory playerInventory, ItemStackCodec payload){
        this(synvId, playerInventory, payload.itemStack());
    }

    // Server Constructor
    public MP4PlayerScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ModScreenHandler.MP4PLAYER_SCREEN_HANDLER, syncId);
        this.itemStack = itemStack;
        this.mp4PlayerInventory = new MP4PlayerInventory(this, itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack());
        this.playerInventory = playerInventory;

        this.addSlot(new MP4PlayerSlot(mp4PlayerInventory, 0, 33, 33));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if(slot != null && slot.hasStack()){
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if(invSlot < this.mp4PlayerInventory.size()){
                if(!this.insertItem(originalStack, this.mp4PlayerInventory.size(), this.slots.size(), true)){
                    return ItemStack.EMPTY;
                }
            } else if(!this.insertItem(originalStack,0, this.mp4PlayerInventory.size(), false)){
                return ItemStack.EMPTY;
            }

            if(originalStack.isEmpty()){
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.mp4PlayerInventory.canPlayerUse(player);
    }

    @Override
    public void onContentChanged(Inventory inventory){
        itemStack.set(ModComponents.ITEMSTACK_COMPONENT, new ItemStackCodec(mp4PlayerInventory.getStack(0)));
        itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal()));
        itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(0));
    }

    public void setRecordingState(){
        itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(0));
        itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().set(ModComponents.RECORDING_COMPONENT, new RecordingComponent());
        itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Recording.ordinal()));
    }

    public void changeIsDirectional(){
        boolean target = !itemStack.get(ModComponents.IS_DIRECTIONAL_COMPONENT).isDirectional();
        itemStack.set(ModComponents.IS_DIRECTIONAL_COMPONENT, new IsDirectionalComponent(target));
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if(id == 0 && itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isOf(ModItems.MICROSD)){
            setRecordingState();
        }
        else if(id == 1){
            changeIsDirectional();
        }
        return super.onButtonClick(player, id);
    }



    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int i=0; i<3; ++i){
            for(int l=0; l<9; ++l){
                this.addSlot(new Slot(playerInventory, l+i*9+9, 8+l*18, 84+i*18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int i=0; i<9; ++i){
            this.addSlot(new Slot(playerInventory, i, 8+i*18, 142));
        }
    }
}
