package com.soundrecording.Payload;

import com.soundrecording.SoundRecordingMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record VolumeSliderC2SPayload(float volume) implements CustomPayload {
    public static final Id<VolumeSliderC2SPayload> ID = new Id<>(Identifier.of(SoundRecordingMod.MOD_ID, "volumeslider-payload"));

    public static final PacketCodec<RegistryByteBuf, VolumeSliderC2SPayload> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.FLOAT, VolumeSliderC2SPayload::volume,
                    VolumeSliderC2SPayload::new);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
