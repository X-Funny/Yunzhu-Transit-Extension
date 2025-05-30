package top.xfunny.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockTicketBarrier;
import org.mtr.mod.block.IBlock;

public class PATTicketBarrier extends BlockTicketBarrier {
    public PATTicketBarrier(boolean isEntrance) {
        super(isEntrance);
    }

    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return IBlock.getVoxelShapeByDirection(12, 0, 0, 16, 16, 16, facing);
    }
}