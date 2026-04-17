package com.soundrecording.Screens.SoundEffectBoard;

import com.soundrecording.Codecs.SoundCodec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;

import java.util.ArrayList;
import java.util.List;

public class SEBListWidget extends AlwaysSelectedEntryListWidget<SEBListEntry> {
    public SEBListWidget(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
    }

    @Override
    public int getRowWidth() {
        return 600;
    }

    @Override
    protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {

    }

    public void updateEntries(List<SoundCodec> sounds, float pitch) {
        this.clearEntries();

        for (int i = 0; i < sounds.size(); i += 3) {
            List<SoundCodec> row = new ArrayList<>();
            SoundCodec s1 = sounds.get(i);
            SoundCodec s2 = (i + 1 < sounds.size()) ? sounds.get(i + 1) : null;
            SoundCodec s3 = (i + 2 < sounds.size()) ? sounds.get(i + 2) : null;

            row.add(s1);
            row.add(s2);
            row.add(s3);

            this.addEntry(new SEBListEntry(row, pitch));
        }

        this.setScrollAmount(0);
    }
}
