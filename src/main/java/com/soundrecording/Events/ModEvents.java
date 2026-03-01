package com.soundrecording.Events;

import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ModEvents {
    private static boolean listenerRegistered = false;

    public ModEvents() {throw new AssertionError();}

    public static void initialize(){
        SoundRecordingMod.LOGGER.info("Adding listeners");

        addSoundListenerListener();
    }

    private static void addSoundListenerListener() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (!listenerRegistered) {
                // By now, soundManager is guaranteed to be initialized
                client.getSoundManager().registerListener(new SoundListener());
                listenerRegistered = true;
            }
        });
    }

}

