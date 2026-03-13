package com.soundrecording.Payload;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ModPayloads {
    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(ItemStackRecordC2SPayload.ID, ItemStackRecordC2SPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(VolumeSliderC2SPayload.ID, VolumeSliderC2SPayload.PACKET_CODEC);

        RegisterItemStackRecordC2SPayload();
        RegisterVolumeSliderC2SPayload();
    }

    static void RegisterItemStackRecordC2SPayload(){
        ServerPlayNetworking.registerGlobalReceiver(ItemStackRecordC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack mp4Stack = player.getInventory().getStack(payload.slotId());
                ItemStack sdStack = mp4Stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack();

                sdStack.set(ModComponents.RECORDING_COMPONENT, recordSound2Component(
                        sdStack.get(ModComponents.RECORDING_COMPONENT), payload, payload.tick()));
                mp4Stack.set(ModComponents.ITEMSTACK_COMPONENT, new ItemStackCodec(sdStack));
            });
        });
    }

    static void RegisterVolumeSliderC2SPayload(){
        ServerPlayNetworking.registerGlobalReceiver(VolumeSliderC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack mp4Stack = player.getMainHandStack();
                mp4Stack.set(ModComponents.VOLUME_COMPONENT, new VolumeComponent(payload.volume()));
            });
        });
    }

    static RecordingComponent recordSound2Component(RecordingComponent rc, ItemStackRecordC2SPayload payload, int tick){
        if(rc == null){
            rc = new RecordingComponent();
        }

        ArrayList<SoundCodec> soundlist = new ArrayList<>(rc.sound());
        ArrayList<PositionCodec> poslist = new ArrayList<>(rc.pos());
        ArrayList<DirectionCodec> dirlist = new ArrayList<>(rc.dir());
        ArrayList<Integer> ticklist = new ArrayList<>(rc.tick());

        soundlist.add(payload.soundPayload());
        poslist.add(payload.posPayload());
        dirlist.add(payload.dirPayload());
        ticklist.add(tick);

        return new RecordingComponent(soundlist, poslist, dirlist, ticklist, rc.size()+1);
    }
}


