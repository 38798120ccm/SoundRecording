package com.soundrecording.Events;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.Items.ModItems;
import com.soundrecording.Payload.ItemStackRecordC2SPayload;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundInstanceListener;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Set;

public class SoundListener implements SoundInstanceListener {
    @Override
    public void onSoundPlayed(SoundInstance sound, WeightedSoundSet soundSet, float range){
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null){return;}
        Vec3d soundPos = new Vec3d(sound.getX(), sound.getY(), sound.getZ());
//        if(sound.getCategory() == SoundCategory.RECORDS){return;}
        if(!player.getPos().isInRange(soundPos, range)){return;}
        System.out.println(sound.getSound().getIdentifier());
        if(player.getInventory().contains(stack -> stack.isOf(ModItems.MP4PLAYER))){
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack stack = player.getInventory().getStack(i);
                if(!stack.isOf(ModItems.MP4PLAYER)){continue;}
                if(stack.get(ModComponents.STATUS_COMPONENT).recordstatus() != MP4PlayerStatus.Recording.ordinal()){continue;}
                if(stack.get(ModComponents.STATUS_COMPONENT).playstatus() != MP4PlayerStatus.Loop.ordinal()){continue;}
                if(stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack() == ItemStack.EMPTY){continue;}
                sendSoundPayloadC2S(sound, player, i, stack.get(ModComponents.TICK_COMPONENT).value());

            }
        }
    }

    void sendSoundPayloadC2S(SoundInstance sound, ClientPlayerEntity player, int slotId, int tick){
        ItemStackRecordC2SPayload payload = new ItemStackRecordC2SPayload(
                new SoundCodec(sound.getId(), sound.getSound().getIdentifier(), sound.getVolume(), sound.getPitch(),
                        sound.getSound().getRegistrationType().name(), sound.getSound().isStreamed(), sound.getSound().getAttenuation(), 0),
                new PositionCodec(sound.getX() - player.getX(), sound.getY() - player.getY(), sound.getZ() - player.getZ()),
                new DirectionCodec(player.getYaw(), player.getPitch()), slotId, tick);
        ClientPlayNetworking.send(payload);
    }
}
