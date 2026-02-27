package com.soundrecording.Blocks;

import com.soundrecording.Items.ModItems;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block Test_BLOCK = registerBlock("test_block",
            new Block(AbstractBlock.Settings.create().strength(4f).requiresTool().sounds(BlockSoundGroup.COPPER)));

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

        ItemGroupEvents.modifyEntriesEvent(ModItems.SOUND_RECORDING_ITEM_GROUP_KEY).register(entries -> {
            entries.add(ModBlocks.Test_BLOCK);
        });
    }

}
