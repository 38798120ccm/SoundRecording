package com.soundrecording.Blocks.Entity;

import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Componets.RecordingComponent;
import com.soundrecording.Componets.TickComponent;
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
        if (world.isClient){
            if(inventory.getFirst().isOf(ModItems.MICROSD)) {
                TickComponent tc = inventory.get(0).get(ModComponents.TICK_COMPONENT);
                if(tc.tick() == 0){return;}
                if(tick <= tc.tick()){
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
        }
    }

    @Environment(EnvType.CLIENT)
    void playPlayerAroundSound(World world, RecordingComponent rc, int tick, BlockPos pos){
        if(world.isClient) {
            MinecraftClient client = MinecraftClient.getInstance();
            int index = Collections.binarySearch(rc.tick(), tick);
            if (index >= 0) {
                for (int i = 0; i < rc.sound().get(index).size(); i++) {
                    PlayerFollowingSoundInstance instance = new PlayerFollowingSoundInstance(client.player,
                            SoundEvent.of(rc.sound().get(index).get(i).eventIdentifier()), SoundCategory.RECORDS,
                            rc.pos().get(index).get(i), rc.dir().get(index).get(i),
                            rc.sound().get(index).get(i).volume() * volume,
                            rc.sound().get(index).get(i).pitch(),
                            true, rc.sound().get(index).get(i));
                    client.getSoundManager().play(instance);
                }
            }
        }
    }

    @Environment(EnvType.CLIENT)
    void playDistanceSound(World world, RecordingComponent rc, int tick, BlockPos pos){
        if(world.isClient){
            MinecraftClient client = MinecraftClient.getInstance();
            if(client.player.getPos().squaredDistanceTo(Vec3d.ofCenter(pos)) > range*range) return;

            int index = Collections.binarySearch(rc.tick(), tick);
            if (index >= 0) {
                for (int i = 0; i < rc.sound().get(index).size(); i++){
                    DistancedSoundInstance instance = new DistancedSoundInstance(client.player, pos,
                            SoundEvent.of(rc.sound().get(index).get(i).eventIdentifier()), SoundCategory.RECORDS,
                            rc.pos().get(index).get(i),
                            rc.sound().get(index).get(i).volume() * volume,
                            rc.sound().get(index).get(i).pitch(),
                            rc.sound().get(index).get(i));
                    client.getSoundManager().play(instance);
                }
            }
        }
    }
}
