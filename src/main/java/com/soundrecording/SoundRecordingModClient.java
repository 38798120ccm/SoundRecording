package com.soundrecording;

import com.soundrecording.Events.ModEvents;
import com.soundrecording.Payload.ModPayloads;
import com.soundrecording.Screens.MP4Player.MP4PlayerScreen;
import com.soundrecording.Screens.ModScreenHandler;
import com.soundrecording.Screens.SoundEffectBoard.SEBScreen;
import com.soundrecording.Screens.SoundEffectCollector.SoundEffectCollectorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class SoundRecordingModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModEvents.initialize();
        ModPayloads.initializeClient();

        HandledScreens.register(ModScreenHandler.MP4PLAYER_SCREEN_HANDLER, MP4PlayerScreen::new);
        HandledScreens.register(ModScreenHandler.SOUND_EFFECT_BOOK_SCREEN_HANDLER_SCREEN_HANDLER, SEBScreen::new);
        HandledScreens.register(ModScreenHandler.SOUND_EFFECT_COLLECTOR_SCREEN_HANDLER_SCREEN_HANDLER, SoundEffectCollectorScreen::new);
    }

}
