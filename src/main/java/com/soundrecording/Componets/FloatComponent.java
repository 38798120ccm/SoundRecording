package com.soundrecording.Componets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FloatComponent(float value) {
    public static final Codec<FloatComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.FLOAT.fieldOf("float").forGetter(FloatComponent::value)
            ).apply(builder, FloatComponent::new)
    );
}
