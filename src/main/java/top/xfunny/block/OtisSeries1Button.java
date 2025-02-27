package top.xfunny.block;


import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.xfunny.BlockEntityTypes;
import top.xfunny.block.base.LiftButtonsBase;

import javax.annotation.Nonnull;
import java.util.List;

public class OtisSeries1Button extends LiftButtonsBase {

    public OtisSeries1Button() {
        super(true, true);
    }


    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(4, 0, 0, 12, 8, 1, IBlock.getStatePropertySafe(state, FACING));
    }


    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OtisSeries1Button.BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        // 添加块的方向属性
        properties.add(FACING);
        // 添加块的解锁状态属性
        properties.add(UNLOCKED);
        properties.add(SINGLE);
    }


    public static class BlockEntity extends BlockEntityBase {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.OTIS_SERIES_1_BUTTON_1.get(), pos, state);
        }
    }
}







