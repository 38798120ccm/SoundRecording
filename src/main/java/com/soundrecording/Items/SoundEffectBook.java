package com.soundrecording.Items;

import com.soundrecording.Codecs.ItemStackCodec;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoundEffectBook extends Item implements ExtendedScreenHandlerFactory<ItemStackCodec> {
    public SoundEffectBook(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public ItemStackCodec getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        ItemStack itemStack = serverPlayerEntity.getStackInHand(Hand.MAIN_HAND);
        return new ItemStackCodec(itemStack);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Sound Effect Book");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }
}
