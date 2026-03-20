package com.soundrecording.Payload;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.Items.ModItems;
import com.soundrecording.Screens.MP4PlayerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModPayloads {
    public static void initializeServer() {
        PayloadTypeRegistry.playC2S().register(ItemStackRecordC2SPayload.ID, ItemStackRecordC2SPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(VolumeSliderC2SPayload.ID, VolumeSliderC2SPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TimelineSliderC2SPayload.ID, TimelineSliderC2SPayload.PACKET_CODEC);

        RegisterItemStackRecordC2SPayload();
        RegisterVolumeSliderC2SPayload();
        RegisterTimelineSliderC2SPayload();

    }

    public static void initializeClient(){
        PayloadTypeRegistry.playS2C().register(MP4ScreenItemStackS2CPayload.ID, MP4ScreenItemStackS2CPayload.PACKET_CODEC);

        RegisterMP4ScreenItemStackS2CPayload();
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
                sdStack.set(ModComponents.TICK_COMPONENT, new TickComponent(payload.tick()));
            });
        });
    }

    static void RegisterVolumeSliderC2SPayload(){
        ServerPlayNetworking.registerGlobalReceiver(VolumeSliderC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack mp4Stack = player.getMainHandStack();
                int slotId = player.getInventory().selectedSlot;
                mp4Stack.set(ModComponents.VOLUME_COMPONENT, new VolumeComponent(payload.volume()));
                MP4ScreenItemStackS2CPayload payload2 = new MP4ScreenItemStackS2CPayload(mp4Stack, slotId, 1);
                ServerPlayNetworking.send(player, payload2);
            });
        });
    }

    static void RegisterTimelineSliderC2SPayload(){
        ServerPlayNetworking.registerGlobalReceiver(TimelineSliderC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack mp4Stack = player.getMainHandStack();
                int slotId = player.getInventory().selectedSlot;
                if(!mp4Stack.isOf(ModItems.MP4PLAYER)) return;
                if(payload.id() == 0){
                    mp4Stack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal(), MP4PlayerStatus.PlayMode.ordinal()));
                }
                else if (payload.id() == 1) {
                    int maxtick = mp4Stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).tick();
                    mp4Stack.set(ModComponents.TICK_COMPONENT, new TickComponent((int)Math.floor(maxtick * payload.percent())));
                    mp4Stack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(payload.prestatus(), MP4PlayerStatus.PlayMode.ordinal()));
                    MP4ScreenItemStackS2CPayload payload2 = new MP4ScreenItemStackS2CPayload(mp4Stack, slotId, 0);
                    ServerPlayNetworking.send(player, payload2);
                }
            });
        });
    }

    @Environment(EnvType.CLIENT)
    static void RegisterMP4ScreenItemStackS2CPayload(){
        ClientPlayNetworking.registerGlobalReceiver(MP4ScreenItemStackS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                int slotId = context.player().getInventory().selectedSlot;
                if (context.client().currentScreen instanceof MP4PlayerScreen screen) {
                    if(slotId != payload.slotId()) return;
                    if(payload.id() == 0){
                        screen.updateData(payload.stack(), 0);
                    }
                    else if(payload.id() == 1){
                        screen.updateVolume(payload.stack());
                    }
                }
            });
        });
    }

    static RecordingComponent recordSound2Component(RecordingComponent rc, ItemStackRecordC2SPayload payload, int tick){
        if(rc == null){
            rc = new RecordingComponent();
        }

        List<List<SoundCodec>> soundlist = new ArrayList<>(rc.sound());
        List<List<PositionCodec>> poslist = new ArrayList<>(rc.pos());
        List<List<DirectionCodec>> dirlist = new ArrayList<>(rc.dir());
        List<Integer> ticklist = new ArrayList<>(rc.tick());

        int index = Collections.binarySearch(ticklist, tick);
        if(index < 0){
            List<SoundCodec> newticksoundlist = new ArrayList<>();
            List<PositionCodec> newtickposlist = new ArrayList<>();
            List<DirectionCodec> newtickdirlist = new ArrayList<>();

            newticksoundlist.add(payload.soundPayload());
            newtickposlist.add(payload.posPayload());
            newtickdirlist.add(payload.dirPayload());

            soundlist.add(newticksoundlist);
            poslist.add(newtickposlist);
            dirlist.add(newtickdirlist);

            ticklist.add(tick);
        }
        else {
            soundlist.get(index).add(payload.soundPayload());
            poslist.get(index).add(payload.posPayload());
            dirlist.get(index).add(payload.dirPayload());
        }
        return new RecordingComponent(soundlist, poslist, dirlist, ticklist, rc.size()+1);
    }
}


