package com.soundrecording.Items;
import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Items.MP4Player.MP4Player;
import com.soundrecording.Items.MP4Player.MP4PlayerStatus;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item MP4PLAYER = register("mp4player", new MP4Player(new MP4Player.Settings().maxCount(1)
            .component(ModComponents.ITEMSTACK_COMPONENT, new ItemStackCodec(ItemStack.EMPTY))
            .component(ModComponents.TICK_COMPONENT, new TickComponent(0))
            .component(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal(), MP4PlayerStatus.PlayMode.ordinal()))
            .component(ModComponents.IS_SOUNDAROUND_COMPONENT, new IsSoundAroundComponent(true))
            .component(ModComponents.VOLUME_COMPONENT, new VolumeComponent(1f))));

    public static final Item MICROSD = register("microsd", new MicroSD(new MicroSD.Settings().maxCount(16)
            .component(ModComponents.RECORDING_COMPONENT, new RecordingComponent())
            .component(ModComponents.TICK_COMPONENT, new TickComponent(0))));

//    public static final Item SOUNDEFFECTBOOK = register("sound_effect_book", new SoundEffectBook(new Item.Settings().maxCount(1)
//            .component(ModComponents.SOUNDLIST_COMPONENT, new SoundListCodec(new ArrayList<>()))));

    public static Item register(String id, Item item){
        // Create the identifier for the item.
        Identifier itemID = Identifier.of(SoundRecordingMod.MOD_ID, id);

        // Create the item instance.
        return Registry.register(Registries.ITEM, itemID, item);
    }


    public static void initialize() {
    }
}
