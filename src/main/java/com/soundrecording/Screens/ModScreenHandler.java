package com.soundrecording.Screens;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;


public class ModScreenHandler {
    public static final ScreenHandlerType<MP4PlayerScreenHandler> MP4PLAYER_SCREEN_HANDLER =
//            register("mp4player_screen_handler", MP4PlayerScreenHandler::new, ItemStackPayload.PACKET_CODEC);
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(SoundRecordingMod.MOD_ID, "mp4player_screen_handler"),
                    new ExtendedScreenHandlerType<>(MP4PlayerScreenHandler::new, ItemStackCodec.PACKET_CODEC));

    public static void initialize(){
        SoundRecordingMod.LOGGER.info("Registering Screen Handlers for " + SoundRecordingMod.MOD_ID);
    }
}

