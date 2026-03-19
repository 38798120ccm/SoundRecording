package com.soundrecording;

import com.soundrecording.Blocks.Entity.ModBlockEntities;
import com.soundrecording.Blocks.ModBlocks;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Events.ModEvents;
import com.soundrecording.Items.ModItemGroups;
import com.soundrecording.Items.ModItems;
import com.soundrecording.Payload.ModPayloads;
import com.soundrecording.Screens.ModScreenHandler;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundRecordingMod implements ModInitializer {
	public static final String MOD_ID = "soundrecording";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModComponents.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModItemGroups.initialize();
		ModScreenHandler.initialize();
		ModEvents.initialize();
		ModPayloads.initialize();

		LOGGER.info("Hello Fabric world!");
	}
}