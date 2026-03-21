package com.soundrecording.Componets;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Codecs.SoundListCodec;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    public static void initialize(){
        SoundRecordingMod.LOGGER.info("Registering {} components", SoundRecordingMod.MOD_ID);
    }

    public static final ComponentType<RecordingComponent> RECORDING_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SoundRecordingMod.MOD_ID, "recording_component"),
            ComponentType.<RecordingComponent>builder().codec(RecordingComponent.CODEC).build()
    );
    public static final ComponentType<ItemStackCodec> ITEMSTACK_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SoundRecordingMod.MOD_ID, "itemstack_component"),
            ComponentType.<ItemStackCodec>builder().codec(ItemStackCodec.CODEC).build()
    );
    public static final ComponentType<TickComponent> TICK_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SoundRecordingMod.MOD_ID, "tick_component"),
            ComponentType.<TickComponent>builder().codec(TickComponent.CODEC).build()
    );
    public static final ComponentType<StatusComponent> STATUS_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SoundRecordingMod.MOD_ID, "status_component"),
            ComponentType.<StatusComponent>builder().codec(StatusComponent.CODEC).build()
    );
    public static final ComponentType<IsSoundAroundComponent> IS_SOUNDAROUND_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SoundRecordingMod.MOD_ID, "issoundaround_component"),
            ComponentType.<IsSoundAroundComponent>builder().codec(IsSoundAroundComponent.CODEC).build()
    );
    public static final ComponentType<VolumeComponent> VOLUME_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SoundRecordingMod.MOD_ID, "volume_component"),
            ComponentType.<VolumeComponent>builder().codec(VolumeComponent.CODEC).build()
    );
    public static final ComponentType<SoundListCodec> SOUNDLIST_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(SoundRecordingMod.MOD_ID, "soundlist_component"),
            ComponentType.<SoundListCodec>builder().codec(SoundListCodec.CODEC).build()
    );

}
