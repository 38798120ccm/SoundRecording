package com.soundrecording.Blocks.Entity;

import com.soundrecording.Items.ModItems;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

//public class SoundEffectCollectorBlockEntity extends BlockEntity implements ImplementedInventory, ExtendedScreenHandlerFactory<> {
//    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
//
//    public SoundEffectCollectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    public DefaultedList<ItemStack> getItems() {
//        return inventory;
//    }
//
//    @Override
//    public boolean isValid(int slot, ItemStack stack) {
//        return stack.isOf(ModItems.MICROSD);
//    }
//
//    @Override
//    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup){
//        super.writeNbt(nbt, registryLookup);
//        Inventories.writeNbt(nbt, inventory, registryLookup);
//    }
//
//    @Override
//    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
//        super.writeNbt(nbt, registryLookup);
//        Inventories.readNbt(nbt, inventory, registryLookup);
//    }
//
//    @Nullable
//    @Override
//    public Packet<ClientPlayPacketListener> toUpdatePacket(){
//        return BlockEntityUpdateS2CPacket.create(this);
//    }
//
//    @Override
//    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup){
//        return createNbt(registryLookup);
//    }
//}
