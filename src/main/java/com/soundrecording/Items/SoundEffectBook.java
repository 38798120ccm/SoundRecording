package com.soundrecording.Items;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Codecs.SoundListCodec;
import com.soundrecording.Componets.ModComponents;
import com.soundrecording.Componets.RecordingComponent;
import com.soundrecording.Screens.SoundEffectBook.SoundEffectBookScreenHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SoundEffectBook extends Item implements ExtendedScreenHandlerFactory<ItemStackCodec> {
    public SoundEffectBook(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient){
            if(user.isSneaking()){
                if(user.getStackInHand(Hand.OFF_HAND).contains(ModComponents.RECORDING_COMPONENT)){
                    Set<SoundCodec> soundCodecs = new TreeSet<>();
                    RecordingComponent rc = user.getStackInHand(Hand.OFF_HAND).get(ModComponents.RECORDING_COMPONENT);
                    if(rc == null) return TypedActionResult.consume(user.getStackInHand(hand));
                    List<List<SoundCodec>> soundlist = new ArrayList<>();
                    for(RecordingComponent.TickData data: rc.tickData().values()) {
                        soundlist.add(data.sounds());
                    }
                    for(int i=0; i<soundlist.size(); i++){
                        soundCodecs.addAll(soundlist.get(i));
                    }
                    user.getStackInHand(Hand.MAIN_HAND).set(ModComponents.SOUNDLIST_COMPONENT, new SoundListCodec(soundCodecs));
                }
            }
            else {
                user.openHandledScreen(this);
            }
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
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
        return new SoundEffectBookScreenHandler(syncId, playerInventory, player.getStackInHand(Hand.MAIN_HAND));
    }

}
