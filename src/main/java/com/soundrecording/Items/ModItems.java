package com.soundrecording.Items;
import com.soundrecording.Blocks.ModBlocks;
import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Items.MP4Player.MP4Player;
import com.soundrecording.SoundRecordingMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;


public class ModItems {
    public static final Item TESTITEM = register("test_item", new TestItem(new Item.Settings()));
    public static final Item MICROPHONE = register("microphone", new Microphone(new Microphone.Settings().maxCount(1)));
    public static final Item MP4PLAYER = register("mp4player", new MP4Player(new MP4Player.Settings().maxCount(1)
            .component(ModComponents.ITEMSTACK_COMPONENT, new ItemStackCodec(ItemStack.EMPTY))
            .component(ModComponents.TICK_COMPONENT, new TickComponent(0))
            .component(ModComponents.STATUS_COMPONENT, new StatusComponent(0))
            .component(ModComponents.IS_DIRECTIONAL_COMPONENT, new IsDirectionalComponent(true))
            .component(ModComponents.VOLUME_COMPONENT, new VolumeComponent(1f))));
    public static final Item MICROSD = register("microsd", new MicroSD(new MicroSD.Settings()
            .component(ModComponents.RECORDING_COMPONENT, new RecordingComponent())
            .component(ModComponents.TICK_COMPONENT, new TickComponent(0))));

    public static Item register(String id, Item item){
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(SoundRecordingMod.MOD_ID, id);

        // Create the item instance.
        return Registry.register(Registries.ITEM, itemID, item);
    }


    public static void initialize() {
    }
}
