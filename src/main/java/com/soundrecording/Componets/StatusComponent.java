package com.soundrecording.Componets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record StatusComponent(int playstatus, int recordstatus) {
    public static final Codec<StatusComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.fieldOf("playstatus").forGetter(StatusComponent::playstatus),
                    Codec.INT.fieldOf("recordstatus").forGetter(StatusComponent::recordstatus)
            ).apply(builder, StatusComponent::new)
    );
}
