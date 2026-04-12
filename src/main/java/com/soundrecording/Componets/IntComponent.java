package com.soundrecording.Componets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record IntComponent(int value) {
    public static final Codec<IntComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.fieldOf("int").forGetter(IntComponent::value)
            ).apply(builder, IntComponent::new)
    );
}
