package com.soundrecording.Screens.MP4Player;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Items.MP4Player.MP4PlayerInventory;
import com.soundrecording.Items.MP4Player.MP4PlayerSlot;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.Payload.MP4ScreenItemStackS2CPayload;
import com.soundrecording.Screens.ModScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

public class MP4PlayerScreenHandler extends ScreenHandler implements ScreenHandlerListener {
    public ItemStack itemStack;
    public final PlayerInventory playerInventory;
    private final MP4PlayerInventory mp4PlayerInventory;

    public MP4PlayerScreenHandler(int synvId, PlayerInventory playerInventory, ItemStackCodec payload){
        this(synvId, playerInventory, payload.itemStack());
    }

    public MP4PlayerScreenHandler(int syncId, PlayerInventory playerInventory, ItemStack itemStack) {
        super(ModScreenHandler.MP4PLAYER_SCREEN_HANDLER, syncId);
        this.itemStack = itemStack;
        this.mp4PlayerInventory = new MP4PlayerInventory(this, itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack());
        this.playerInventory = playerInventory;

        this.addSlot(new MP4PlayerSlot(mp4PlayerInventory, 0, 33, 33, itemStack));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addListener(this);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if(slot.hasStack()){
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

    public void setRecordingState(){
        itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(0));
        itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().set(ModComponents.TICK_COMPONENT, new TickComponent(0));
        itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().set(ModComponents.RECORDING_COMPONENT, new RecordingComponent());
        itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Loop.ordinal(),MP4PlayerStatus.Recording.ordinal()));
    }

    public void stopRecordingState(){
        itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().set(ModComponents.TICK_COMPONENT, new TickComponent(itemStack.get(ModComponents.TICK_COMPONENT).tick()));
        itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(0));
        itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal(),MP4PlayerStatus.PlayMode.ordinal()));
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        switch (id){
            case 0:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                stopRecordingState();
                break;
            case 1:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                setRecordingState();
                break;
            case 10:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                int status = itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus();
                itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal(), status));
                break;
            case 11:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.RECORDING_COMPONENT).size() <= 0) break;
                int status2 = itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus();
                itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Loop.ordinal(), status2));
                break;
            case 20:
                itemStack.set(ModComponents.IS_SOUNDAROUND_COMPONENT, new IsSoundAroundComponent(false));
                break;
            case 21:
                itemStack.set(ModComponents.IS_SOUNDAROUND_COMPONENT, new IsSoundAroundComponent(true));
                break;
            case 30:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                int newtick30 = Math.min(itemStack.get(ModComponents.TICK_COMPONENT).tick() + 10,
                        itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).tick());
                itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(newtick30));
                break;
            case 31:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                int newtick31 = Math.max(itemStack.get(ModComponents.TICK_COMPONENT).tick() - 10, 0);
                itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(newtick31));
                break;
            case 40:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                int maxtick40 = itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).tick();
                int newtick40 = Math.min(itemStack.get(ModComponents.TICK_COMPONENT).tick() + (int)(maxtick40*0.2), maxtick40);
                itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(newtick40));
                break;
            case 41:
                if(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) break;
                int maxtick41 = itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).tick();
                int newtick41 = Math.max(itemStack.get(ModComponents.TICK_COMPONENT).tick() - (int)(maxtick41*0.2), 0);
                itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(newtick41));
                break;
        }
        if(!player.getWorld().isClient){
            MP4ScreenItemStackS2CPayload payload = new MP4ScreenItemStackS2CPayload(itemStack, player.getInventory().selectedSlot);
            ServerPlayNetworking.send((ServerPlayerEntity)player, payload);
        }
        sendContentUpdates();
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

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
        if(mp4PlayerInventory.getStack(0).isEmpty()){}
        else if(ItemStack.areEqual(itemStack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack(), mp4PlayerInventory.getStack(0))) return;
        else if(itemStack.get(ModComponents.STATUS_COMPONENT).recordstatus() == MP4PlayerStatus.Recording.ordinal()) return;

        itemStack.set(ModComponents.ITEMSTACK_COMPONENT, new ItemStackCodec(mp4PlayerInventory.getStack(0)));
        itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal(), MP4PlayerStatus.PlayMode.ordinal()));
        itemStack.set(ModComponents.TICK_COMPONENT, new TickComponent(0));

//        if(!playerInventory.player.getWorld().isClient){
//            MP4ScreenItemStackS2CPayload payload = new MP4ScreenItemStackS2CPayload(itemStack, playerInventory.player.);
//            ServerPlayNetworking.send((ServerPlayerEntity)playerInventory.player, payload);
//        }
    }


    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {

    }
}
