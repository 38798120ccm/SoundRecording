package com.soundrecording.Payload;

import com.soundrecording.SoundRecordingMod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MP4ScreenItemStackS2CPayload(ItemStack stack, int slotID) implements CustomPayload {
    public static final Id<MP4ScreenItemStackS2CPayload> ID = new Id<>(Identifier.of(SoundRecordingMod.MOD_ID, "mp4screenitemstacks2c-payload"));

    public static final PacketCodec<RegistryByteBuf, MP4ScreenItemStackS2CPayload> PACKET_CODEC =
            PacketCodec.tuple(
                    ItemStack.OPTIONAL_PACKET_CODEC, MP4ScreenItemStackS2CPayload::stack,
                    PacketCodecs.INTEGER, MP4ScreenItemStackS2CPayload::slotID,
                    MP4ScreenItemStackS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
