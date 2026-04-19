package com.soundrecording.Screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.util.Identifier;

public class StateButton extends TexturedButtonWidget {
    protected final ButtonTextures textures0;
    protected final ButtonTextures textures1;
    protected int textureid;

    public StateButton(int x, int y, int width, int height, ButtonTextures textures0, ButtonTextures textures1, int id, PressAction pressAction) {
        super(x, y, width, height, textures0, pressAction);
        this.textures0 = textures0;
        this.textures1 = textures1;
        this.textureid = id;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        ButtonTextures selected = (textureid == 0)? textures0: textures1;
        Identifier identifier = selected.get(this.isNarratable(), this.isSelected());
        context.drawGuiTexture(identifier, this.getX(), this.getY(), this.width, this.height);
    }

    public void switchTexture(int textureid){
        this.textureid = textureid;
    }
}
