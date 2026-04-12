package com.soundrecording.Componets;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BooleanComponent(boolean value) {
    public static final Codec<BooleanComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.BOOL.fieldOf("boolean").forGetter(BooleanComponent::value)
            ).apply(builder, BooleanComponent::new)
    );
}
