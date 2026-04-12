package com.soundrecording.Blocks.Entity;

import com.soundrecording.Blocks.ModBlocks;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<SpeakerBlockEntity> SPEAKER_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(SoundRecordingMod.MOD_ID, "speaker_be"),
                    BlockEntityType.Builder.create(SpeakerBlockEntity::new, ModBlocks.SPEAKER_BLOCK).build(null));

    public static final BlockEntityType<SoundEffectCollectorBlockEntity> SOUNDEFFECTCOLLECTOR_BE =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(SoundRecordingMod.MOD_ID, "soundeffectcollector_be"),
                BlockEntityType.Builder.create(SoundEffectCollectorBlockEntity::new, ModBlocks.SOUNDEFFECTCOLLECTOR_BLOCK).build(null));

    public static void initialize(){
        SoundRecordingMod.LOGGER.info("Registering Block Entities for " + SoundRecordingMod.MOD_ID);
    }
}
