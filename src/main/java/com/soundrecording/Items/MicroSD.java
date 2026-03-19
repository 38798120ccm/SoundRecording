package com.soundrecording.Items;

import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Componets.RecordingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class MicroSD extends Item {
    public MicroSD(Settings settings){
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type){
        super.appendTooltip(stack, context, tooltip, type);
        if (stack.contains(ModComponents.RECORDING_COMPONENT)){
            RecordingComponent recordingComponent = stack.get(ModComponents.RECORDING_COMPONENT);
            int size = recordingComponent.size();
            tooltip.add(Text.translatable("Sound Count: " + size));
        }
        if(stack.contains(ModComponents.TICK_COMPONENT)){
            int length_min = stack.get(ModComponents.TICK_COMPONENT).tick()/1200;
            int length_sec = (stack.get(ModComponents.TICK_COMPONENT).tick()/20)%60;
            String length = String.format("%02d:%02d", length_min, length_sec);
            tooltip.add(Text.translatable("Length: " + length));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
