package com.soundrecording.Screens;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Screens.MP4Player.MP4PlayerScreenHandler;
import com.soundrecording.Screens.SoundEffectBook.SoundEffectBookScreenHandler;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;


public class ModScreenHandler {
    public static final ScreenHandlerType<MP4PlayerScreenHandler> MP4PLAYER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(SoundRecordingMod.MOD_ID, "mp4player_screen_handler"),
                    new ExtendedScreenHandlerType<>(MP4PlayerScreenHandler::new, ItemStackCodec.PACKET_CODEC));

    public static final ScreenHandlerType<SoundEffectBookScreenHandler> SOUND_EFFECT_BOOK_SCREEN_HANDLER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(SoundRecordingMod.MOD_ID, "sound_effect_book_screen_handler"),
                    new ExtendedScreenHandlerType<>(SoundEffectBookScreenHandler::new, ItemStackCodec.PACKET_CODEC));

    public static void initialize(){
        SoundRecordingMod.LOGGER.info("Registering Screen Handlers for " + SoundRecordingMod.MOD_ID);
    }
}

