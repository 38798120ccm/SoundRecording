package com.soundrecording.Codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soundrecording.SoundRecordingMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SoundCodec(Identifier eventIdentifier, Identifier soundIdentifier, float volume, float pitch,
                         String registrationType, boolean stream, int attenuation, int ticksSinceSongStarted) implements CustomPayload{

    public static final Codec<SoundCodec> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Identifier.CODEC.fieldOf("eventIdentifier").forGetter(SoundCodec::eventIdentifier),
                    Identifier.CODEC.fieldOf("soundIdentifier").forGetter(SoundCodec::soundIdentifier),
                    Codec.FLOAT.fieldOf("volume").forGetter(SoundCodec::volume),
                    Codec.FLOAT.fieldOf("pitch").forGetter(SoundCodec::pitch),
                    Codec.STRING.fieldOf("registrationType").forGetter(SoundCodec::registrationType),
                    Codec.BOOL.fieldOf("stream").forGetter(SoundCodec::stream),
                    Codec.INT.fieldOf("attenuation").forGetter(SoundCodec::attenuation),
                    Codec.INT.fieldOf("ticksSinceSongStarted").forGetter(SoundCodec::ticksSinceSongStarted)
            ).apply(builder, SoundCodec::new)
    );

    public static final CustomPayload.Id<SoundCodec> ID = new Id<>(Identifier.of(SoundRecordingMod.MOD_ID, "sound-payload"));

    public static final PacketCodec<RegistryByteBuf, SoundCodec> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public SoundCodec decode(RegistryByteBuf buf) {

            var f1 = buf.readIdentifier();
            var f2 = buf.readIdentifier();
            var f3 = buf.readFloat();
            var f4 = buf.readFloat();
            var f5 = buf.readString();
            var f6 = buf.readBoolean();
            var f7 = buf.readInt();
            var f8 = buf.readInt();

            return new SoundCodec(f1, f2, f3, f4, f5, f6, f7, f8);
        }

        @Override
        public void encode(RegistryByteBuf buf, SoundCodec value) {
            buf.writeIdentifier(value.eventIdentifier);
            buf.writeIdentifier(value.soundIdentifier);
            buf.writeFloat(value.volume);
            buf.writeFloat(value.pitch);
            buf.writeString(value.registrationType);
            buf.writeBoolean(value.stream);
            buf.writeInt(value.attenuation);
            buf.writeInt(value.ticksSinceSongStarted);
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() { return ID; }
}
