package com.soundrecording.SoundInstance;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import net.minecraft.client.sound.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.floatprovider.FloatSupplier;
import net.minecraft.util.math.random.Random;

public class PlayerFollowingSoundInstance extends MovingSoundInstance {

    private final LivingEntity entity;
    private final PositionCodec pos;
    private final DirectionCodec dir;
    private final boolean Isdirectional;
    public PlayerFollowingSoundInstance(LivingEntity entity, SoundEvent soundEvent, SoundCategory soundCategory, PositionCodec pos,
                                        DirectionCodec dir, float volume, float pitch, boolean Isdirectional, SoundCodec soundCodec) {
        super(soundEvent, soundCategory, SoundInstance.createRandom());
        this.entity = entity;
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = false;
        this.pos = pos;
        this.dir = dir;
        this.Isdirectional = Isdirectional;
        this.sound = new Sound(soundCodec.soundIdentifier(), (random) -> 1.0f, (random) -> 1.0f, 1,
                Sound.RegistrationType.getByName(soundCodec.registrationType()),
                soundCodec.stream(), false, soundCodec.attenuation());
        this.setPositionToEntity();
    }

    @Override
    public WeightedSoundSet getSoundSet(SoundManager soundManager) {
        if (this.id.equals(SoundManager.INTENTIONALLY_EMPTY_ID)) {
            this.sound = SoundManager.INTENTIONALLY_EMPTY_SOUND;
            return SoundManager.INTENTIONALLY_EMPTY_SOUND_SET;
        } else {
            WeightedSoundSet weightedSoundSet = soundManager.get(this.id);
            if (weightedSoundSet == null) {
                this.sound = SoundManager.MISSING_SOUND;
            }
            return weightedSoundSet;
        }
    }

    @Override
    public void tick() {
        if (this.entity == null || this.entity.isRemoved() || this.entity.isDead()) {
            this.setDone();
            return;
        }
        this.setPositionToEntity();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    private void setPositionToEntity() {
        double diff = Math.toRadians(entity.getYaw() - dir.yaw());
        this.x = this.entity.getX() + (Isdirectional? pos.x() * Math.cos(diff) - pos.z() * Math.sin(diff):pos.x());
        this.y = this.entity.getY() + pos.y();
        this.z = this.entity.getZ() + (Isdirectional? pos.x() * Math.sin(diff) + pos.z() * Math.cos(diff):pos.z());
    }
}
