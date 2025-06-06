package top.xfunny.mod.item;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.BlockLiftButtons;
import org.mtr.mod.block.BlockLiftPanelBase;
import org.mtr.mod.block.BlockLiftTrackBase;
import org.mtr.mod.block.BlockLiftTrackFloor;
import org.mtr.mod.item.ItemBlockClickingBase;
import top.xfunny.mod.LiftFloorRegistry;
import top.xfunny.mod.block.base.LiftButtonsBase;
import top.xfunny.mod.block.base.LiftDestinationDispatchTerminalBase;
import top.xfunny.mod.block.base.LiftPanelBase;

import static top.xfunny.mod.item.LinkerValidTypes.VALID_TYPES;

public class YteGroupLiftButtonsLinker extends ItemBlockClickingBase implements DirectionHelper {
    private final boolean isConnector;
    BlockPos posStart;
    int number;

    public YteGroupLiftButtonsLinker(boolean isConnector, ItemSettings itemSettings) {
        super(itemSettings.maxCount(1));
        this.isConnector = isConnector;
    }

    private static void connect(World world, BlockPos blockPos1, BlockPos blockPos2, boolean isAdd) {
        final BlockEntity blockEntity1 = world.getBlockEntity(blockPos1);
        final BlockEntity blockEntity2 = world.getBlockEntity(blockPos2);

        if (blockEntity1 != null && blockEntity2 != null) {
            if (blockEntity2.data instanceof BlockLiftTrackFloor.BlockEntity) {
                if (blockEntity1.data instanceof LiftFloorRegistry) {
                    ((LiftFloorRegistry) blockEntity1.data).registerFloor(blockPos1, world, blockPos2, isAdd);
                } else if (blockEntity1.data instanceof BlockLiftButtons.BlockEntity) {
                    ((BlockLiftButtons.BlockEntity) blockEntity1.data).registerFloor(blockPos2, isAdd);
                } else if (blockEntity1.data instanceof BlockLiftPanelBase.BlockEntityBase) {
                    ((BlockLiftPanelBase.BlockEntityBase) blockEntity1.data).registerFloor(world, blockPos2, isAdd);
                }
            } else if (blockEntity2.data instanceof LiftButtonsBase.BlockEntityBase || blockEntity2.data instanceof LiftDestinationDispatchTerminalBase.BlockEntityBase) {
                //todo
            }
        }
    }

    @Override
    protected void onStartClick(@NotNull ItemUsageContext context, @NotNull CompoundTag compoundTag) {
    }

    @Override
    protected void onEndClick(ItemUsageContext context, @NotNull BlockPos posEnd, @NotNull CompoundTag compoundTag) {
        final PathFinder pathFinder = new PathFinder();
        final PlayerEntity playerEntity = context.getPlayer();
        int floorCount = 0;
        final World world = context.getWorld();
        posStart = context.getBlockPos();
        number = 0;


        while (true) {
            // 退出循环
            if (posStart != null && (world.getBlockState(posStart).getBlock().data instanceof BlockLiftTrackBase ||
                    world.getBlockState(posEnd).getBlock().data instanceof BlockLiftTrackBase)) {
                connect(world, posStart, posEnd, isConnector);
                connect(world, posEnd, posStart, isConnector);
                Object[] pos = pathFinder.findPath(context, posStart, posEnd);
                posStart = (BlockPos) pos[0];
                posEnd = (BlockPos) pos[1];

                if (world.getBlockState(posEnd).getBlock().data instanceof LiftButtonsBase ||
                        world.getBlockState(posEnd).getBlock().data instanceof LiftDestinationDispatchTerminalBase ||
                        world.getBlockState(posEnd).getBlock().data instanceof LiftPanelBase ||
                        world.getBlockState(posEnd).getBlock().data instanceof BlockLiftButtons ||
                        world.getBlockState(posEnd).getBlock().data instanceof BlockLiftPanelBase
                ) {
                    floorCount++;
                }

            } else {
                if (playerEntity != null) {
                    playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_failed")) : Text.cast(TextHelper.translatable("message.linker_status_failed_remove")), true);
                }
                break;
            }
            if (number == pathFinder.getMark().size()) {
                if (playerEntity != null) {
                    playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_finished", floorCount)) : Text.cast(TextHelper.translatable("message.linker_status_finished_remove", floorCount)), true);
                }
                break;
            }
            number++;
        }
    }

    private boolean isValidType(Object data) {
        return VALID_TYPES.contains(data.getClass());
    }

    @Override
    protected boolean clickCondition(ItemUsageContext context) {
        final Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        return isValidType(block.data);
    }

}