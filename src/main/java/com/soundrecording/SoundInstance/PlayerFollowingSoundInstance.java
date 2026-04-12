package com.soundrecording.SoundInstance;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import net.minecraft.client.sound.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class PlayerFollowingSoundInstance extends MovingSoundInstance {

    private final LivingEntity entity;
    private final PositionCodec pos;
    private final DirectionCodec dir;
    private final boolean issoundaround;

    public PlayerFollowingSoundInstance(LivingEntity entity, SoundCodec soundCodec, PositionCodec pos,
                                        DirectionCodec dir, SoundCategory soundCategory, float volume, float pitch, boolean issoundaround) {
        super(SoundEvent.of(soundCodec.eventIdentifier()), soundCategory, SoundInstance.createRandom());
        this.entity = entity;
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = false;
        this.pos = pos;
        this.dir = dir;
        this.issoundaround = issoundaround;
        this.sound = new Sound(soundCodec.soundIdentifier(), (random) -> 1.0f, (random) -> 1.0f, 1,
                Sound.RegistrationType.getByName(soundCodec.registrationType()),
                soundCodec.stream(), true, soundCodec.attenuation());
        this.setPositionToEntity();
    }

    public PlayerFollowingSoundInstance(LivingEntity entity, SoundCodec soundCodec, SoundCategory soundCategory, float pitch){
        super(SoundEvent.of(soundCodec.eventIdentifier()), soundCategory, SoundInstance.createRandom());
        this.entity = entity;
        this.volume = 1.0f;
        this.pitch = pitch;
        this.repeat = false;
        this.pos = null;
        this.dir = null;
        this.issoundaround = false;
        this.sound = new Sound(soundCodec.soundIdentifier(), (random) -> 1.0f, (random) -> 1.0f, 1,
                Sound.RegistrationType.getByName(soundCodec.registrationType()),
                soundCodec.stream(), true, soundCodec.attenuation());
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
        if(pos == null || dir == null){
            this.x = this.entity.getX();
            this.y = this.entity.getY();
            this.z = this.entity.getZ();
            return;
        }
        double diff = Math.toRadians(entity.getYaw() - dir.yaw());
        this.x = this.entity.getX() + (issoundaround? pos.x() * Math.cos(diff) - pos.z() * Math.sin(diff):0);
        this.y = this.entity.getY() + (issoundaround? pos.y(): 0);
        this.z = this.entity.getZ() + (issoundaround? pos.x() * Math.sin(diff) + pos.z() * Math.cos(diff):0);
    }
}
