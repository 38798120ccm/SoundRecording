package com.soundrecording.Items;

import com.soundrecording.Blocks.ModBlocks;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> SOUND_RECORDING_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(SoundRecordingMod.MOD_ID, "item_group"));
    public static final ItemGroup SOUND_RECORDING_ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.MP4PLAYER))
            .displayName(Text.translatable("itemGroup.soundrecording"))
            .build();

    public static void initialize() {
        Registry.register(Registries.ITEM_GROUP,SOUND_RECORDING_ITEM_GROUP_KEY, SOUND_RECORDING_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(SOUND_RECORDING_ITEM_GROUP_KEY).register(itemGroup -> {
            itemGroup.add(ModItems.MP4PLAYER);
            itemGroup.add(ModItems.MICROSD);
            itemGroup.add(ModItems.SOUNDEFFECTBOARD);
        });

        ItemGroupEvents.modifyEntriesEvent(SOUND_RECORDING_ITEM_GROUP_KEY).register(entries -> {
            entries.add(ModBlocks.SPEAKER_BLOCK);
            entries.add(ModBlocks.SOUNDEFFECTCOLLECTOR_BLOCK);
        });
    }
}
