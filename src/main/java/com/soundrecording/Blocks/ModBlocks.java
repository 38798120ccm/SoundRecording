package com.soundrecording.Blocks;

import com.soundrecording.SoundRecordingMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block SPEAKER_BLOCK = registerBlock("speaker_block",
            new SpeakerBlock(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.COPPER)));
    public static final Block SOUNDEFFECTCOLLECTOR_BLOCK = registerBlock("soundeffectcollector_block",
            new SoundEffectCollectorBlock(AbstractBlock.Settings.create().strength(1f).sounds(BlockSoundGroup.COPPER).nonOpaque()));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(SoundRecordingMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, Identifier.of(SoundRecordingMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void initialize(){
        SoundRecordingMod.LOGGER.info("Registering Mod Blocks for " + SoundRecordingMod.MOD_ID);
    }

}
