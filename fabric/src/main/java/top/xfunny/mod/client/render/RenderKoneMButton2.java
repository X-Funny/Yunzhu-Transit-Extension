package top.xfunny.mod.client.render;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.StoredMatrixTransformations;
import top.xfunny.mod.Init;
import top.xfunny.mod.block.KoneMButton2;
import top.xfunny.mod.block.SchindlerMSeriesButton;
import top.xfunny.mod.block.base.LiftButtonsBase;
import top.xfunny.mod.client.view.*;
import top.xfunny.mod.client.view.view_group.FrameLayout;
import top.xfunny.mod.client.view.view_group.LinearLayout;
import top.xfunny.mod.item.YteGroupLiftButtonsLinker;
import top.xfunny.mod.item.YteLiftButtonsLinker;
import top.xfunny.mod.keymapping.DefaultButtonsKeyMapping;

public class RenderKoneMButton2 extends BlockEntityRenderer<KoneMButton2.BlockEntity> implements DirectionHelper, IGui, IBlock {

    private static final int DEFAULT_COLOR = 0xFFFFFFFF;
    private final int HOVER_COLOR = 0xFFDDDDDD;
    private final int PRESSED_COLOR = 0xFFCC0000;
    private final Identifier BUTTON_TEXTURE_UP = new Identifier(Init.MOD_ID, "textures/block/kone_m_button_2_up_b.png");
    private final Identifier BUTTON_TEXTURE_DOWN = new Identifier(Init.MOD_ID, "textures/block/kone_m_button_2_down_b.png");
    private final Identifier LIGHT_TEXTURE = new Identifier(Init.MOD_ID, "textures/block/kone_m_button_2_light.png");

    public RenderKoneMButton2(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(KoneMButton2.BlockEntity blockEntity, float tickDelta, GraphicsHolder graphicsHolder1, int light, int overlay) {
        final World world = blockEntity.getWorld2();
        if (world == null) {
            return;
        }

        final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
        if (clientPlayerEntity == null) {
            return;
        }

        final DefaultButtonsKeyMapping keyMapping = blockEntity.getKeyMapping();

        final BlockPos blockPos = blockEntity.getPos2();
        final BlockState blockState = world.getBlockState(blockPos);
        final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
        final boolean holdingLinker = PlayerHelper.isHolding(PlayerEntity.cast(clientPlayerEntity), item -> item.data instanceof YteLiftButtonsLinker || item.data instanceof YteGroupLiftButtonsLinker);
        LiftButtonsBase.LiftButtonDescriptor buttonDescriptor = new LiftButtonsBase.LiftButtonDescriptor(false, false);

        final StoredMatrixTransformations storedMatrixTransformations1 = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        storedMatrixTransformations1.add(graphicsHolder -> {
            graphicsHolder.rotateYDegrees(-facing.asRotation());
            graphicsHolder.translate(0, 0, 7.9F / 16 - SMALL_OFFSET);
        });

        final FrameLayout parentLayout = new FrameLayout();
        parentLayout.setBasicsAttributes(world, blockPos);
        parentLayout.setStoredMatrixTransformations(storedMatrixTransformations1);
        parentLayout.setParentDimensions(3.2F / 16, 5.5F / 16);
        parentLayout.setPosition(-1.6F / 16, 0);
        parentLayout.setWidth(LayoutSize.MATCH_PARENT);
        parentLayout.setHeight(LayoutSize.MATCH_PARENT);

        final LinearLayout buttonContainer = new LinearLayout(true);
        buttonContainer.setBasicsAttributes(world, blockPos);
        buttonContainer.setWidth(LayoutSize.WRAP_CONTENT);
        buttonContainer.setHeight(LayoutSize.WRAP_CONTENT);
        buttonContainer.setGravity(Gravity.CENTER);

        final FrameLayout upButtonGroup = new FrameLayout();
        upButtonGroup.setBasicsAttributes(world, blockPos);
        upButtonGroup.setStoredMatrixTransformations(storedMatrixTransformations1);
        upButtonGroup.setWidth(LayoutSize.WRAP_CONTENT);
        upButtonGroup.setHeight(LayoutSize.WRAP_CONTENT);
        upButtonGroup.setGravity(Gravity.CENTER_HORIZONTAL);

        final FrameLayout downButtonGroup = new FrameLayout();
        downButtonGroup.setBasicsAttributes(world, blockPos);
        downButtonGroup.setStoredMatrixTransformations(storedMatrixTransformations1);
        downButtonGroup.setWidth(LayoutSize.WRAP_CONTENT);
        downButtonGroup.setHeight(LayoutSize.WRAP_CONTENT);
        downButtonGroup.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView buttonUp = new ImageView();
        buttonUp.setBasicsAttributes(world, blockPos);
        buttonUp.setTexture(BUTTON_TEXTURE_UP);
        buttonUp.setDimension(1.25F / 16, 353, 178);
        buttonUp.setGravity(Gravity.CENTER);
        buttonUp.setLight(light);

        ButtonView buttonUpLight = new ButtonView();
        buttonUpLight.setId("up");
        buttonUpLight.setBasicsAttributes(world, blockPos, keyMapping);
        buttonUpLight.setTexture(LIGHT_TEXTURE);
        buttonUpLight.setDimension(1.25F / 16, 353, 178);
        buttonUpLight.setGravity(Gravity.CENTER);
        buttonUpLight.setLight(light);
        buttonUpLight.setDefaultColor(DEFAULT_COLOR);
        buttonUpLight.setHoverColor(HOVER_COLOR);
        buttonUpLight.setPressedColor(PRESSED_COLOR);

        ImageView buttonDown = new ImageView();
        buttonDown.setBasicsAttributes(world, blockPos);
        buttonDown.setTexture(BUTTON_TEXTURE_DOWN);
        buttonDown.setDimension(1.25F / 16, 353, 178);
        buttonDown.setGravity(Gravity.CENTER);
        buttonDown.setLight(light);
        buttonDown.setFlip(false, false);

        ButtonView buttonDownLight = new ButtonView();
        buttonDownLight.setId("down");
        buttonDownLight.setBasicsAttributes(world, blockPos, keyMapping);
        buttonDownLight.setTexture(LIGHT_TEXTURE);
        buttonDownLight.setDimension(1.25F / 16, 353, 178);
        buttonDownLight.setGravity(Gravity.CENTER);
        buttonDownLight.setLight(light);
        buttonDownLight.setDefaultColor(DEFAULT_COLOR);
        buttonDownLight.setHoverColor(HOVER_COLOR);
        buttonDownLight.setPressedColor(PRESSED_COLOR);

        final LineComponent line = new LineComponent();
        line.setBasicsAttributes(world, blockPos);

        final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, Lift>> sortedPositionsAndLifts = new ObjectArrayList<>();

        blockEntity.forEachTrackPosition(trackPosition -> {
            line.RenderLine(holdingLinker, trackPosition);

            SchindlerMSeriesButton.hasButtonsClient(trackPosition, buttonDescriptor, (floorIndex, lift) -> {
                sortedPositionsAndLifts.add(new ObjectObjectImmutablePair<>(trackPosition, lift));
                final ObjectArraySet<LiftDirection> instructionDirections = lift.hasInstruction(floorIndex);
                instructionDirections.forEach(liftDirection -> {
                    switch (liftDirection) {
                        case DOWN:
                            buttonDownLight.activate();
                            break;
                        case UP:
                            buttonUpLight.activate();
                            break;
                    }
                });
            });
        });

        upButtonGroup.addChild(buttonUp);
        upButtonGroup.addChild(buttonUpLight);
        downButtonGroup.addChild(buttonDown);
        downButtonGroup.addChild(buttonDownLight);

        if (buttonDescriptor.hasUpButton()) {
            buttonContainer.addChild(upButtonGroup);
        }

        if (buttonDescriptor.hasDownButton()) {
            if (buttonDescriptor.hasUpButton()) {
                downButtonGroup.setMargin(0, 0.5F / 16, 0, 0);
            }
            buttonContainer.addChild(downButtonGroup);
        }

        parentLayout.addChild(buttonContainer);
        parentLayout.render();
    }
}
