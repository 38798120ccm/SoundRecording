package com.soundrecording.Componets;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RecordingComponent(Int2ObjectMap<TickData> tickData, int size) {

    public RecordingComponent() {
        this(new Int2ObjectOpenHashMap<>(), 0);
    }

    public record TickData(List<SoundCodec> sounds, List<PositionCodec> positions, List<DirectionCodec> directions) {
        public static final Codec<TickData> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        SoundCodec.CODEC.listOf().fieldOf("sounds").forGetter(TickData::sounds),
                        PositionCodec.CODEC.listOf().fieldOf("positions").forGetter(TickData::positions),
                        DirectionCodec.CODEC.listOf().fieldOf("directions").forGetter(TickData::directions)
                ).apply(builder, TickData::new)
        );
    }

    public static final Codec<Int2ObjectMap<TickData>> MAP_CODEC =
            Codec.unboundedMap(Codec.STRING, TickData.CODEC).xmap(
                    stringMap -> {
                        Int2ObjectMap<TickData> fastMap = new Int2ObjectOpenHashMap<>();
                        stringMap.forEach((s, data) -> {
                            try {
                                fastMap.put(Integer.parseInt(s), data);
                            } catch (NumberFormatException e) {
                            }
                        });
                        return fastMap;
                    },
                    fastMap -> {
                        Map<String, TickData> stringMap = new HashMap<>();
                        fastMap.forEach((tick, data) -> {
                            stringMap.put(String.valueOf(tick), data);
                        });
                        return stringMap;
                    }
            );

    public static final Codec<RecordingComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    MAP_CODEC.fieldOf("tickdata").forGetter(RecordingComponent::tickData),
                    Codec.INT.fieldOf("size").forGetter(RecordingComponent::size)
            ).apply(builder, RecordingComponent::new)
    );
}


