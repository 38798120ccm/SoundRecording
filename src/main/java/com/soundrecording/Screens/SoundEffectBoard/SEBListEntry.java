package com.soundrecording.Screens.SoundEffectBoard;

import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.SoundInstance.PlayerFollowingSoundInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;

import java.util.List;

public class SEBListEntry extends AlwaysSelectedEntryListWidget.Entry<SEBListEntry> {
    private final List<SoundCodec> sounds;
    private final MinecraftClient client;
    private int columnWidth;
    private int OffsetX;
    private final float pitch;


    public SEBListEntry(List<SoundCodec> sounds, float pitch) {
        this.sounds = sounds;
        this.client = MinecraftClient.getInstance();
        this.pitch = pitch;
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        columnWidth = entryWidth / 3;
        OffsetX = x;

        for (int i=0; i<sounds.size(); i++) {
            int X = (OffsetX + i*columnWidth);
            SoundCodec sound = sounds.get(i);

            boolean isMouseOverColumn = (mouseX>=X && mouseX<X+columnWidth && mouseY>=y && mouseY<y+entryHeight);
            if (isMouseOverColumn) {
                context.fill(X , y , X + columnWidth - 2, y + entryHeight - 2, 0x44FFFFFF);
            }

            if(sound == null){return;}
            String soundName = sound.soundIdentifier().getPath().substring(sound.soundIdentifier().getPath().indexOf('/') + 1) + " " + sound.pitch();
            context.drawText(client.textRenderer, soundName, X, y + (entryHeight / 2) - 4, 0xFFFFFF, false);
        }
    }

    @Override
    public Text getNarration() {
        return Text.literal("Sound Effect Entry");
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int index;
        if(mouseX > OffsetX && mouseX < OffsetX + columnWidth){
            index = 0;
        }
        else if(mouseX > OffsetX + columnWidth && mouseX < OffsetX + 2*columnWidth){
            index = 1;
        }
        else if(mouseX > OffsetX + 2*columnWidth && mouseX < OffsetX + 3*columnWidth){
            index = 2;
        }
        else {
            return true;
        }
        PlayerFollowingSoundInstance soundInstance = new PlayerFollowingSoundInstance(
                client.player, sounds.get(index), SoundCategory.MASTER, pitch);
        client.getSoundManager().play(soundInstance);
        return true;
    }
}
