package com.soundrecording.Items.MP4Player;

import com.soundrecording.Codecs.DirectionCodec;
import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Codecs.PositionCodec;
import com.soundrecording.Codecs.SoundCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Payload.MP4ScreenItemStackS2CPayload;
import com.soundrecording.Screens.MP4Player.MP4PlayerScreenHandler;
import com.soundrecording.SoundInstance.PlayerFollowingSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class MP4Player extends Item implements ExtendedScreenHandlerFactory<ItemStackCodec>{

    public MP4Player(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type){
        super.appendTooltip(stack, context, tooltip, type);
        if(stack.contains(ModComponents.STATUS_COMPONENT)){
            StatusComponent statusComponent = stack.get(ModComponents.STATUS_COMPONENT);
            String key = "";
            if(MP4PlayerStatus.Recording.ordinal() == statusComponent.recordstatus()){key = "Mode: Recording";}
            else {
                if (MP4PlayerStatus.Idle.ordinal() == statusComponent.playstatus()) {
                    key = "Mode: Idle";
                }
                if (MP4PlayerStatus.Loop.ordinal() == statusComponent.playstatus()) {
                    key = "Mode: Loop";
                }
            }
            tooltip.add(Text.translatable(key).formatted(Formatting.GOLD));
        }

        if(stack.contains(ModComponents.TICK_COMPONENT)) {
            int tick = stack.get(ModComponents.TICK_COMPONENT).value();
            int current_hr =  tick/72000;
            int current_min = tick/1200;
            int current_sec = (tick/20)%60;
            String current = String.format("%02d:%02d:%02d", current_hr, current_min, current_sec);
            tooltip.add(Text.literal("Current: " + current).formatted(Formatting.GOLD));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(Hand.MAIN_HAND);
        if (!world.isClient) {
            ItemStackCodec itemStackCodec = itemStack.get(ModComponents.ITEMSTACK_COMPONENT);
            StatusComponent statusComponent = itemStack.get(ModComponents.STATUS_COMPONENT);
            if(user.isSneaking()) {
                    user.openHandledScreen(this);
            }
            else if(itemStackCodec.itemStack().contains(ModComponents.TICK_COMPONENT)){
                if(statusComponent.playstatus() == MP4PlayerStatus.Idle.ordinal()){
                    itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Loop.ordinal(), statusComponent.recordstatus()));
                }
                else if(statusComponent.playstatus() == MP4PlayerStatus.Loop.ordinal()){
                    itemStack.set(ModComponents.STATUS_COMPONENT, new StatusComponent(MP4PlayerStatus.Idle.ordinal(), statusComponent.recordstatus()));
                }
            }
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!stack.contains(ModComponents.ITEMSTACK_COMPONENT)) return;
        if(!stack.contains(ModComponents.TICK_COMPONENT)) return;
        if(!stack.contains(ModComponents.STATUS_COMPONENT)) return;
        if(stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) return;

        StatusComponent statusComponent = stack.get(ModComponents.STATUS_COMPONENT);
        IntComponent tickComponent = stack.get(ModComponents.TICK_COMPONENT);
        int nexttick = tickComponent.value() + 1;

        if(statusComponent.playstatus() == MP4PlayerStatus.Loop.ordinal()){
            if(statusComponent.recordstatus() == MP4PlayerStatus.Recording.ordinal()){
                stack.set(ModComponents.TICK_COMPONENT, new IntComponent(nexttick));
            }
            else if(statusComponent.recordstatus() == MP4PlayerStatus.PlayMode.ordinal()){
                if(tickComponent.value() > stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).value()){
                    stack.set(ModComponents.TICK_COMPONENT, new IntComponent(0));
                }
                else {
                    playTickSound(world, stack, tickComponent.value());
                    stack.set(ModComponents.TICK_COMPONENT, new IntComponent(nexttick));

                    if(world.isClient) return;
                    if(entity instanceof ServerPlayerEntity serverPlayer){
                        if (serverPlayer.currentScreenHandler instanceof MP4PlayerScreenHandler handler){
                            MP4ScreenItemStackS2CPayload payload = new MP4ScreenItemStackS2CPayload(stack, slot);
                            ServerPlayNetworking.send(serverPlayer, payload);
                        }
                    }
                }
            }
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BRUSH;
    }

    @Override
    public ItemStackCodec getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        ItemStack itemStack = serverPlayerEntity.getStackInHand(Hand.MAIN_HAND);
        return new ItemStackCodec(itemStack);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("MP4Player");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new MP4PlayerScreenHandler(syncId, playerInventory, player.getStackInHand(Hand.MAIN_HAND));
    }

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    void playTickSound(World world ,ItemStack stack, int tick) {
        if(!world.isClient) return;
        BooleanComponent soundAroundComp = stack.get(ModComponents.IS_SOUNDAROUND_COMPONENT);
        ItemStackCodec itemStackComp = stack.get(ModComponents.ITEMSTACK_COMPONENT);
        FloatComponent volumeComp = stack.get(ModComponents.VOLUME_COMPONENT);
        if(soundAroundComp == null || itemStackComp == null || volumeComp == null) return;

        RecordingComponent rc = itemStackComp.itemStack().get(ModComponents.RECORDING_COMPONENT);
        if (rc == null) return;
        RecordingComponent.TickData data = rc.tickData().get(tick);
        if (data == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        for (int i=0; i<data.sounds().size(); i++) {
            PlayerFollowingSoundInstance instance = new PlayerFollowingSoundInstance(
                    client.player,
                    data.sounds().get(i),
                    data.positions().get(i),
                    data.directions().get(i),
                    SoundCategory.RECORDS,
                    data.sounds().get(i).volume() * volumeComp.value(),
                    data.sounds().get(i).pitch(),
                    soundAroundComp.value()
            );
            client.getSoundManager().play(instance);
        }
    }
}
