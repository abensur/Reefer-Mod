package com.abensur.badhabits;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("null")
public class EmptyTearLockerBlock extends Block {
    private static final BooleanProperty CHARGED = BooleanProperty.create("charged");
    private static final BooleanProperty NORTH = BooleanProperty.create("north");
    private static final BooleanProperty EAST = BooleanProperty.create("east");
    private static final BooleanProperty SOUTH = BooleanProperty.create("south");
    private static final BooleanProperty WEST = BooleanProperty.create("west");

    private static final BlockState HEAVY_CORE_STATE = Blocks.HEAVY_CORE.defaultBlockState();
    private static final int COMPLETION_DELAY_TICKS = 40;
    private static final Map<GlobalPos, UUID> PENDING_REWARDS = new HashMap<>();

    private static final Map<Direction, BooleanProperty> DIRECTION_TO_PROPERTY;

    static {
        Map<Direction, BooleanProperty> map = new EnumMap<>(Direction.class);
        map.put(Direction.NORTH, NORTH);
        map.put(Direction.EAST, EAST);
        map.put(Direction.SOUTH, SOUTH);
        map.put(Direction.WEST, WEST);
        DIRECTION_TO_PROPERTY = Collections.unmodifiableMap(map);
    }

    public EmptyTearLockerBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(1.2f, 2.0f)
            .sound(SoundType.HEAVY_CORE)
            .lightLevel(EmptyTearLockerBlock::calculateLight)
        );

        this.registerDefaultState(this.stateDefinition.any()
            .setValue(CHARGED, Boolean.FALSE)
            .setValue(NORTH, Boolean.FALSE)
            .setValue(EAST, Boolean.FALSE)
            .setValue(SOUTH, Boolean.FALSE)
            .setValue(WEST, Boolean.FALSE)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CHARGED, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return HEAVY_CORE_STATE.getShape(level, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return HEAVY_CORE_STATE.getCollisionShape(level, pos, context);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(CHARGED)) {
            return;
        }

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.6;
        double centerZ = pos.getZ() + 0.5;

        level.addParticle(ParticleTypes.GLOW, centerX + (random.nextDouble() - 0.5) * 0.3, centerY + random.nextDouble() * 0.3,
            centerZ + (random.nextDouble() - 0.5) * 0.3, 0.0, 0.01, 0.0);
        level.addParticle(ParticleTypes.ENCHANT, centerX + (random.nextDouble() - 0.5) * 0.6, centerY,
            centerZ + (random.nextDouble() - 0.5) * 0.6, 0.0, 0.02, 0.0);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(CHARGED)) {
            return;
        }

        GlobalPos key = GlobalPos.of(level.dimension(), pos);
        UUID playerId = PENDING_REWARDS.remove(key);
        Player player = playerId != null ? level.getPlayerByUUID(playerId) : null;
        finishLocker(level, pos, player);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(CHARGED)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        Direction face = hit.getDirection();
        if (!face.getAxis().isHorizontal()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!stack.is(Items.GHAST_TEAR)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BooleanProperty property = DIRECTION_TO_PROPERTY.get(face);
        if (property == null || state.getValue(property)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide()) {
            BlockState updatedState = state.setValue(property, Boolean.TRUE);
            level.setBlock(pos, updatedState, Block.UPDATE_ALL);

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            playChargeFeedback(level, pos, face);

            if (allSidesActive(updatedState)) {
                startCompletionCountdown(level, pos, player, updatedState);
            }
        }

        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    private static boolean allSidesActive(BlockState state) {
        return state.getValue(NORTH) && state.getValue(EAST) && state.getValue(SOUTH) && state.getValue(WEST);
    }

    private void startCompletionCountdown(Level level, BlockPos pos, Player player, BlockState state) {
        if (!(level instanceof ServerLevel serverLevel) || state.getValue(CHARGED)) {
            return;
        }

        BlockState chargedState = state.setValue(CHARGED, Boolean.TRUE);
        level.setBlock(pos, chargedState, Block.UPDATE_ALL);

        GlobalPos key = GlobalPos.of(serverLevel.dimension(), pos);
        PENDING_REWARDS.put(key, player.getUUID());

        serverLevel.scheduleTick(pos, this, COMPLETION_DELAY_TICKS);
        level.playSound(null, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 0.8F, 0.85F);

        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5,
            28, 0.4, 0.3, 0.4, 0.02);
        serverLevel.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5,
            40, 0.5, 0.4, 0.5, 0.08);
    }

    private static int calculateLight(BlockState state) {
        int level = 0;
        if (state.getValue(NORTH)) level += 4;
        if (state.getValue(EAST)) level += 4;
        if (state.getValue(SOUTH)) level += 4;
        if (state.getValue(WEST)) level += 4;
        return Math.min(15, level);
    }

    private void playChargeFeedback(Level level, BlockPos pos, Direction face) {
        level.playSound(null, pos, SoundEvents.AMETHYST_CLUSTER_PLACE, SoundSource.BLOCKS, 0.6F, 1.4F);

        if (level instanceof ServerLevel serverLevel) {
            double centerX = pos.getX() + 0.5 + 0.52 * face.getStepX();
            double centerY = pos.getY() + 0.5;
            double centerZ = pos.getZ() + 0.5 + 0.52 * face.getStepZ();

            serverLevel.sendParticles(ParticleTypes.SOUL, centerX, centerY, centerZ, 6, 0.08, 0.12, 0.08, 0.01);
            serverLevel.sendParticles(ParticleTypes.GLOW, centerX, centerY, centerZ, 2, 0.04, 0.05, 0.04, 0.02);
        }
    }

    private void finishLocker(Level level, BlockPos pos, @Nullable Player player) {
        level.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 0.9F, 1.2F);
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 0.5F, 1.0F);

        if (level instanceof ServerLevel serverLevel) {
            var charm = BadHabits.TEAR_LOCKER.get().getDefaultInstance();

            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5,
                24, 0.4, 0.4, 0.4, 0.02);
            serverLevel.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5,
                18, 0.3, 0.3, 0.3, 0.6);

            boolean delivered = player != null && player.addItem(charm);
            if (!delivered) {
                Block.popResource(serverLevel, pos, charm);
            }
        }

        level.removeBlock(pos, false);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);

        if (!level.isClientSide() && !player.isCreative() && level instanceof ServerLevel serverLevel) {
            Block.popResource(serverLevel, pos, BadHabits.EMPTY_TEAR_LOCKER_BLOCK_ITEM.get().getDefaultInstance());
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide() && state.getBlock() != newState.getBlock() && level instanceof ServerLevel serverLevel) {
            PENDING_REWARDS.remove(GlobalPos.of(serverLevel.dimension(), pos));
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
