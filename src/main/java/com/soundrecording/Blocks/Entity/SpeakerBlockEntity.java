package com.soundrecording.Blocks.Entity;

import com.soundrecording.Componets.IntComponent;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Componets.RecordingComponent;
import com.soundrecording.Items.ModItems;
import com.soundrecording.SoundInstance.DistancedSoundInstance;
import com.soundrecording.SoundInstance.PlayerFollowingSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

import static com.soundrecording.Blocks.SpeakerBlock.POWERED;

public class SpeakerBlockEntity extends BlockEntity implements ImplementedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    public int tick = 0;
    public float volume = 1f;
    public float range = 15;

    public SpeakerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPEAKER_BE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isOf(ModItems.MICROSD);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup){
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, inventory, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.readNbt(nbt, inventory, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket(){
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup){
        return createNbt(registryLookup);
    }

    public void tick(World world, BlockPos pos, BlockState state1) {
        if(this.getCachedState().get(POWERED)) return;
        if(!world.isClient) return;
        if(!inventory.getFirst().isOf(ModItems.MICROSD)) return;

        IntComponent tc = inventory.get(0).get(ModComponents.TICK_COMPONENT);
        if(tc.value() == 0){return;}
        if(tick <= tc.value()){
            RecordingComponent rc = inventory.get(0).get(ModComponents.RECORDING_COMPONENT);
            if(true){
                playDistanceSound(world, rc, tick, pos);
            }
            else{
                playPlayerAroundSound(world, rc, tick, pos);
            }
            tick++;
        }
        else{
            tick = 0;
        }
    }

    @Environment(EnvType.CLIENT)
    void playPlayerAroundSound(World world, RecordingComponent rc, int tick, BlockPos pos){
        if(!world.isClient) return;
        if (rc == null) return;
        RecordingComponent.TickData data = rc.tickData().get(tick);
        if (data == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        for (int i=0; i<data.sounds().size(); i++) {
            PlayerFollowingSoundInstance instance = new PlayerFollowingSoundInstance(
                    client.player,
                    data.sounds().get(i),
                    data.positions().get(i),
                    data.directions().get(i),
                    SoundCategory.RECORDS,
                    data.sounds().get(i).volume() * volume,
                    data.sounds().get(i).pitch(),
                    true);
            client.getSoundManager().play(instance);
        }
    }

    @Environment(EnvType.CLIENT)
    void playDistanceSound(World world, RecordingComponent rc, int tick, BlockPos pos){
        if(!world.isClient) return;
        if (rc == null) return;
        RecordingComponent.TickData data = rc.tickData().get(tick);
        if (data == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) > range*range) return;
        for (int i=0; i<data.sounds().size(); i++) {
            DistancedSoundInstance instance = new DistancedSoundInstance(
                    client.player, pos,
                    data.sounds().get(i),
                    data.positions().get(i),
                    SoundCategory.RECORDS,
                    data.sounds().get(i).volume() * volume,
                    data.sounds().get(i).pitch());
            client.getSoundManager().play(instance);
        }

    }
}
