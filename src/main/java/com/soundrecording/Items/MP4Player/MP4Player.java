package com.soundrecording.Items.MP4Player;

import com.soundrecording.Codecs.ItemStackCodec;
import com.soundrecording.Componets.*;
import com.soundrecording.Payload.MP4ScreenItemStackS2CPayload;
import com.soundrecording.Screens.MP4PlayerScreenHandler;
import com.soundrecording.SoundInstance.PlayerFollowingSoundInstance;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.client.MinecraftClient;
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
            int current_min = stack.get(ModComponents.TICK_COMPONENT).tick()/120;
            int current_sec = (stack.get(ModComponents.TICK_COMPONENT).tick()/20)%60;
            String current = String.format("%02d:%02d", current_min, current_sec);
            tooltip.add(Text.literal("Current: " + current).formatted(Formatting.GOLD));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(Hand.MAIN_HAND);
        if (!world.isClient) {
            TickComponent tickComponent = itemStack.get(ModComponents.TICK_COMPONENT);
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
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!stack.contains(ModComponents.ITEMSTACK_COMPONENT)) return;
        if(!stack.contains(ModComponents.TICK_COMPONENT)) return;
        if(!stack.contains(ModComponents.STATUS_COMPONENT)) return;
        if(stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().isEmpty()) return;

        StatusComponent statusComponent = stack.get(ModComponents.STATUS_COMPONENT);
        TickComponent tickComponent = stack.get(ModComponents.TICK_COMPONENT);
        int nexttick = tickComponent.tick() + 1;

        if(statusComponent.playstatus() == MP4PlayerStatus.Loop.ordinal()){
            if(statusComponent.recordstatus() == MP4PlayerStatus.Recording.ordinal()){
                stack.set(ModComponents.TICK_COMPONENT, new TickComponent(nexttick));
            }
            else if(statusComponent.recordstatus() == MP4PlayerStatus.PlayMode.ordinal()){
                if(tickComponent.tick() > stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.TICK_COMPONENT).tick()){
                    stack.set(ModComponents.TICK_COMPONENT, new TickComponent(0));
                }
                else {
                    playTickSound(world, stack, tickComponent.tick());
                    stack.set(ModComponents.TICK_COMPONENT, new TickComponent(nexttick));

                    if(world.isClient) return;
                    if(entity instanceof ServerPlayerEntity serverPlayer){
                        if (serverPlayer.currentScreenHandler instanceof MP4PlayerScreenHandler handler){
                            if (world.getTime() % 10 == 0) {
                                MP4ScreenItemStackS2CPayload payload = new MP4ScreenItemStackS2CPayload(stack, slot, 0);
                                ServerPlayNetworking.send(serverPlayer, payload);
                            }
                        }
                    }
                }
            }
        }
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

    void playTickSound(World world, ItemStack stack, int tick){
        if(world.isClient){
            boolean issoundaround = stack.get(ModComponents.IS_SOUNDAROUND_COMPONENT).issoundaround();
            RecordingComponent rc = stack.get(ModComponents.ITEMSTACK_COMPONENT).itemStack().get(ModComponents.RECORDING_COMPONENT);
            float volume = stack.get(ModComponents.VOLUME_COMPONENT).volume();
            MinecraftClient client = MinecraftClient.getInstance();

            int index = Collections.binarySearch(rc.tick(), tick);
            if(index >= 0){
                for(int i=0; i<rc.sound().get(index).size(); i++){
                    PlayerFollowingSoundInstance instance = new PlayerFollowingSoundInstance(client.player,
                            SoundEvent.of(rc.sound().get(index).get(i).eventIdentifier()), SoundCategory.RECORDS,
                            rc.pos().get(index).get(i), rc.dir().get(index).get(i),
                            rc.sound().get(index).get(i).volume() * volume,
                            rc.sound().get(index).get(i).pitch(),
                            issoundaround, rc.sound().get(index).get(i));
                    client.getSoundManager().play(instance);
                }
            }
        }
    }
}
