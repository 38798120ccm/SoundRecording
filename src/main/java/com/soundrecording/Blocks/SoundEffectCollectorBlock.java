package com.soundrecording.Blocks;

import com.mojang.serialization.MapCodec;
import com.soundrecording.Blocks.Entity.SoundEffectCollectorBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SoundEffectCollectorBlock extends BlockWithEntity implements BlockEntityProvider {
    private static final VoxelShape SHAPE =
            SoundEffectCollectorBlock.createCuboidShape(2, 0, 2, 14, 13, 14);
    public static final MapCodec<SoundEffectCollectorBlock> CODEC = SoundEffectCollectorBlock.createCodec(SoundEffectCollectorBlock::new);

    protected SoundEffectCollectorBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SoundEffectCollectorBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof SoundEffectCollectorBlockEntity entity){
            if(!world.isClient){
                player.openHandledScreen(entity);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
       if(state.getBlock() != newState.getBlock()){
           BlockEntity blockEntity = world.getBlockEntity(pos);
           if(blockEntity instanceof SoundEffectCollectorBlockEntity){
               ItemScatterer.spawn(world, pos, ((SoundEffectCollectorBlockEntity) blockEntity));
               world.updateComparators(pos, this);
           }
           super.onStateReplaced(state, world, pos, newState, moved);
       }
    }
}
