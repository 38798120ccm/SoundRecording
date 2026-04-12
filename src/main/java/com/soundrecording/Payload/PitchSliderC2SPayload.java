package com.soundrecording.Payload;

import com.soundrecording.SoundRecordingMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record PitchSliderC2SPayload(float pitch) implements CustomPayload {
    public static final Id<PitchSliderC2SPayload> ID = new Id<>(Identifier.of(SoundRecordingMod.MOD_ID, "pitchsliderc2s-payload"));

    public static final PacketCodec<RegistryByteBuf, PitchSliderC2SPayload> PACKET_CODEC =
            PacketCodec.tuple(
                    PacketCodecs.FLOAT, PitchSliderC2SPayload::pitch,
                    PitchSliderC2SPayload::new);
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
