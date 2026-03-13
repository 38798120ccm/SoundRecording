package com.soundrecording.SoundInstance;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class DistancedSoundInstance extends MovingSoundInstance {
    private final LivingEntity entity;
    private final BlockPos blockPos;
    private final PositionCodec pos;

    public DistancedSoundInstance(LivingEntity entity, BlockPos blockPos, SoundEvent soundEvent, SoundCategory soundCategory,
                                  PositionCodec pos, float volume, float pitch, SoundCodec soundCodec) {
        super(soundEvent, soundCategory, SoundInstance.createRandom());
        this.entity = entity;
        this.blockPos = blockPos;
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = false;
        this.pos = pos;
        this.sound = new Sound(soundCodec.soundIdentifier(), (random) -> 1.0f, (random) -> 1.0f, 1,
                Sound.RegistrationType.getByName(soundCodec.registrationType()),
                soundCodec.stream(), false, soundCodec.attenuation());
    }

    @Override
    public void tick() {
        if (this.entity == null || this.entity.isRemoved() || this.entity.isDead()) {
            this.setDone();
            return;
        }
        this.setPosition();
    }

    @Override
    public boolean shouldAlwaysPlay() {
        return true;
    }

    void setPosition(){
        double distance = Math.sqrt(pos.x()*pos.x() + pos.y()*pos.y() + pos.z()*pos.z());
        Vec3d blockCenter = Vec3d.ofCenter(blockPos);
        Vec3d direction = blockCenter.subtract(entity.getPos()).normalize();
        x = blockCenter.x + direction.x * distance;
        y = blockCenter.y + direction.y * distance;
        z = blockCenter.z + direction.z * distance;
    }
}
