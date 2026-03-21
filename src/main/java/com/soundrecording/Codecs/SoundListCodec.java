package com.soundrecording.Codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record SoundListCodec(List<SoundCodec> soundCodecsList) implements CustomPayload {
    public static final Codec<SoundListCodec> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    SoundCodec.CODEC.listOf().fieldOf("soundList").forGetter(SoundListCodec::soundCodecsList)
            ).apply(builder, SoundListCodec::new)
    );

    public static final CustomPayload.Id<SoundListCodec> ID = new CustomPayload.Id<>(Identifier.of(SoundRecordingMod.MOD_ID, "soundlist-payload"));

    public static final PacketCodec<RegistryByteBuf, SoundListCodec> PACKET_CODEC = PacketCodec.tuple(
            SoundCodec.PACKET_CODEC.collect(PacketCodecs.toList()), SoundListCodec::soundCodecsList,
            SoundListCodec::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}