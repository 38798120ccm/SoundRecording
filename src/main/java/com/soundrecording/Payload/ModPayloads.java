package com.soundrecording.Payload;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Items.MP4Player.MP4Player;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.Items.ModItems;
import com.soundrecording.Screens.MP4Player.MP4PlayerScreen;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModPayloads {
    public static void initializeServer() {
        PayloadTypeRegistry.playC2S().register(ItemStackRecordC2SPayload.ID, ItemStackRecordC2SPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(VolumeSliderC2SPayload.ID, VolumeSliderC2SPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(TimelineSliderC2SPayload.ID, TimelineSliderC2SPayload.PACKET_CODEC);
        PayloadTypeRegistry.playC2S().register(PitchSliderC2SPayload.ID, PitchSliderC2SPayload.PACKET_CODEC);

        RegisterItemStackRecordC2SPayload();
        RegisterVolumeSliderC2SPayload();
        RegisterTimelineSliderC2SPayload();
        RegisterPitchSliderC2SPayload();
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
                sdStack.set(ModComponents.TICK_COMPONENT, new IntComponent(payload.tick()));
            });
        });
    }

    static void RegisterVolumeSliderC2SPayload(){
        ServerPlayNetworking.registerGlobalReceiver(VolumeSliderC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack stack = player.getMainHandStack();
                if(!stack.isOf(ModItems.MP4PLAYER)) return;
                stack.set(ModComponents.VOLUME_COMPONENT, new FloatComponent(payload.volume()));
            });
        });
    }
    static void RegisterPitchSliderC2SPayload(){
        ServerPlayNetworking.registerGlobalReceiver(PitchSliderC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack stack = player.getMainHandStack();
                if(!stack.isOf(ModItems.SOUNDEFFECTBOOK)) return;
                stack.set(ModComponents.PITCH_COMPONENT, new FloatComponent(payload.pitch()));
            });
        });
    }

    static void RegisterTimelineSliderC2SPayload(){
        ServerPlayNetworking.registerGlobalReceiver(TimelineSliderC2SPayload.ID, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayerEntity player = context.player();
                ItemStack mp4Stack = player.getMainHandStack();
                if(!mp4Stack.isOf(ModItems.MP4PLAYER)) return;
                int maxtick = mp4Stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).value();
                if(payload.id() == 0){
                    mp4Stack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal(), MP4PlayerStatus.PlayMode.ordinal()));
                }
                else if (payload.id() == 1) {
                    mp4Stack.set(ModComponents.TICK_COMPONENT, new IntComponent((int)Math.floor(maxtick * payload.percent())));
                }
                else if(payload.id() == 2){
                    mp4Stack.set(ModComponents.TICK_COMPONENT, new IntComponent((int)Math.floor(maxtick * payload.percent())));
                    mp4Stack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(payload.prestatus(), MP4PlayerStatus.PlayMode.ordinal()));
                }
            });
        });
    }

    @Environment(EnvType.CLIENT)
    static void RegisterMP4ScreenItemStackS2CPayload(){
        ClientPlayNetworking.registerGlobalReceiver(MP4ScreenItemStackS2CPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                if(context.player().getInventory().selectedSlot != payload.slotID()) return;
                if (context.client().currentScreen instanceof MP4PlayerScreen screen) {
                    screen.updateData(payload.stack());
                }
            });
        });
    }

    static RecordingComponent recordSound2Component(RecordingComponent rc, ItemStackRecordC2SPayload payload, int tick){
        if(rc == null){
            rc = new RecordingComponent();
        }
        Int2ObjectMap<RecordingComponent.TickData> newMap = new Int2ObjectOpenHashMap<>(rc.tickData());
        RecordingComponent.TickData tickData = newMap.computeIfAbsent(tick, k ->
                new RecordingComponent.TickData(new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );

        tickData.sounds().add(payload.soundPayload());
        tickData.positions().add(payload.posPayload());
        tickData.directions().add(payload.dirPayload());

        return new RecordingComponent(newMap, rc.size() + 1);
    }
}


