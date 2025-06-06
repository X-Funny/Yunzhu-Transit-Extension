package top.xfunny.mod.client.render;


import org.mtr.core.data.Lift;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import top.xfunny.mod.Init;
import top.xfunny.mod.block.SchindlerDSeriesScreen1Even;
import top.xfunny.mod.block.base.LiftPanelBase;
import top.xfunny.mod.client.resource.FontList;
import top.xfunny.mod.client.view.*;
import top.xfunny.mod.client.view.view_group.FrameLayout;
import top.xfunny.mod.item.YteGroupLiftButtonsLinker;
import top.xfunny.mod.item.YteLiftButtonsLinker;

import java.util.Comparator;

public class RenderOtisSeries3ELDScreen1<T extends LiftPanelBase.BlockEntityBase> extends BlockEntityRenderer<T> implements DirectionHelper, IGui, IBlock {
    private final boolean isOdd;

    public RenderOtisSeries3ELDScreen1(Argument dispatcher, Boolean isOdd) {
        super(dispatcher);
        this.isOdd = isOdd;
    }

    @Override
    public void render(T blockEntity, float tickDelta, GraphicsHolder graphicsHolder1, int light, int overlay) {
        final World world = blockEntity.getWorld2();
        if (world == null) {
            return;
        }

        final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
        if (clientPlayerEntity == null) {
            return;
        }

        final boolean holdingLinker = PlayerHelper.isHolding(PlayerEntity.cast(clientPlayerEntity), item -> item.data instanceof YteLiftButtonsLinker || item.data instanceof YteGroupLiftButtonsLinker);
        final BlockPos blockPos = blockEntity.getPos2();
        final BlockState blockState = world.getBlockState(blockPos);
        final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        StoredMatrixTransformations storedMatrixTransformations1 = storedMatrixTransformations.copy();
        storedMatrixTransformations1.add(graphicsHolder -> {
            graphicsHolder.rotateYDegrees(-facing.asRotation());
            graphicsHolder.translate(0, 0, 7.65F / 16 - SMALL_OFFSET);
        });

        final FrameLayout parentLayout = new FrameLayout();
        parentLayout.setBasicsAttributes(world, blockEntity.getPos2());
        parentLayout.setStoredMatrixTransformations(storedMatrixTransformations1);
        parentLayout.setParentDimensions((float) 4 / 16, (float) 2.3 / 16);
        parentLayout.setPosition(isOdd ? -1F / 16 : -9F / 16, 11.375F / 16);
        parentLayout.setWidth(LayoutSize.MATCH_PARENT);
        parentLayout.setHeight(LayoutSize.MATCH_PARENT);

        final LineComponent line = new LineComponent();
        line.setBasicsAttributes(world, blockEntity.getPos2());

        final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, Lift>> sortedPositionsAndLifts = new ObjectArrayList<>();

        blockEntity.forEachTrackPosition(trackPosition -> {
            line.RenderLine(holdingLinker, trackPosition);
            SchindlerDSeriesScreen1Even.LiftCheck(trackPosition, (floorIndex, lift) -> {
                sortedPositionsAndLifts.add(new ObjectObjectImmutablePair<>(trackPosition, lift));
            });
        });

        sortedPositionsAndLifts.sort(Comparator.comparingInt(sortedPositionAndLift -> blockEntity.getPos2().getManhattanDistance(new Vector3i(sortedPositionAndLift.left().data))));

        if (!sortedPositionsAndLifts.isEmpty()) {
            final int count = 1;

            for (int i = 0; i < count; i++) {
                final LiftFloorDisplayView liftFloorDisplayView = new LiftFloorDisplayView();
                liftFloorDisplayView.setBasicsAttributes(world,
                        blockEntity.getPos2(),
                        sortedPositionsAndLifts.get(i).right(),
                        FontList.instance.getFont("nimbus_sans_bold"),
                        6,
                        0xFFB29B3C);
                liftFloorDisplayView.setTextureId("otis_series_3_eld_1_screen_display");
                liftFloorDisplayView.setWidth((float) 3 / 16);
                liftFloorDisplayView.setHeight((float) 2.3 / 16);
                liftFloorDisplayView.setGravity(Gravity.CENTER);
                liftFloorDisplayView.setTextAlign(TextView.HorizontalTextAlign.CENTER, TextView.VerticalTextAlign.CENTER);
                liftFloorDisplayView.setLetterSpacing(0);
                liftFloorDisplayView.setAdaptMode(LiftFloorDisplayView.AdaptMode.FIT_WIDTH);
                liftFloorDisplayView.setMargin(5F / 16, 0, 0, 0);
                liftFloorDisplayView.addStoredMatrixTransformations(graphicsHolder -> graphicsHolder.translate(0, 0, -SMALL_OFFSET));

                final LiftArrowView liftArrowView = new LiftArrowView();
                liftArrowView.setBasicsAttributes(world, blockEntity.getPos2(), sortedPositionsAndLifts.get(i).right(), LiftArrowView.ArrowType.AUTO);
                liftArrowView.setTexture(new Identifier(Init.MOD_ID, "textures/block/otis_s3_eld_arrow_1.png"));
                liftArrowView.setDimension(0.65F / 16);
                liftArrowView.setQueuedRenderLayer(QueuedRenderLayer.LIGHT_TRANSLUCENT);
                liftArrowView.setMargin(0.425F / 16, 2.5F / 16, 0, 0);
                liftArrowView.setColor(0xFFB29B3C);

                parentLayout.addChild(liftArrowView);
                parentLayout.addChild(liftFloorDisplayView);
            }
        }
        parentLayout.render();
    }
}