package com.soundrecording.Codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soundrecording.SoundRecordingMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.*;

public record SoundListCodec(Set<SoundCodec> soundIdentifiers) implements CustomPayload {
    public static final Codec<SoundListCodec> CODEC = SoundCodec.CODEC.listOf().xmap(
                list -> new SoundListCodec(new TreeSet<>(list)),
                component -> new ArrayList<>(component.soundIdentifiers())
            );

    public static final CustomPayload.Id<SoundListCodec> ID = new CustomPayload.Id<>(Identifier.of(SoundRecordingMod.MOD_ID, "soundlist-payload"));

    public static final PacketCodec<RegistryByteBuf, SoundListCodec> PACKET_CODEC = PacketCodec.tuple(
            SoundCodec.PACKET_CODEC.collect(PacketCodecs.toCollection(HashSet::new)), SoundListCodec::soundIdentifiers,
            SoundListCodec::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}