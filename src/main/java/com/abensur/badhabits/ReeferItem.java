package com.abensur.badhabits;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ReeferItem extends Item {
    public ReeferItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        // Add effect information
        tooltipComponents.add(Component.translatable("item.badhabits.reefer.tooltip.boost").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.translatable("item.badhabits.reefer.tooltip.crash").withStyle(ChatFormatting.RED));

        // Add durability/uses information
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        int usesRemaining = maxDamage - damage;
        tooltipComponents.add(Component.translatable("item.badhabits.reefer.tooltip.uses", usesRemaining, maxDamage).withStyle(ChatFormatting.DARK_GRAY));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public SoundEvent getBreakingSound() {
        return SoundEvents.CANDLE_EXTINGUISH;
    }

    @Override
    @SuppressWarnings("null")
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        // Update custom model data for burning state
        updateCustomModelData(stack, true);

        // Play candle extinguish sound when starting to use
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.CANDLE_EXTINGUISH, SoundSource.PLAYERS, 0.5F, 1.0F);

        return InteractionResultHolder.consume(stack);
    }

    @SuppressWarnings("null")
    private void updateCustomModelData(ItemStack stack, boolean isUsing) {
        if (!stack.isDamageableItem()) {
            return;
        }

        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        int remaining = maxDamage - damage;

        int customModelData;
        // Stage 0: Fresh (8 uses, not burning)
        // Stage 1: Fresh Burning (8 uses, actively smoking)
        // Stage 2: Smoked (5-7 uses, not burning)
        // Stage 3: Smoked Burning (5-7 uses, actively smoking)
        // Stage 4: Half (3-4 uses, not burning)
        // Stage 5: Half Burning (3-4 uses, actively smoking)
        // Stage 6: Roach (1-2 uses, not burning)
        // Stage 7: Roach Burning (1-2 uses, actively smoking)

        if (remaining >= 8) {
            customModelData = isUsing ? 1 : 0;  // Fresh / Fresh Burning
        } else if (remaining >= 5) {
            customModelData = isUsing ? 3 : 2;  // Smoked / Smoked Burning
        } else if (remaining >= 3) {
            customModelData = isUsing ? 5 : 4;  // Half / Half Burning
        } else {
            customModelData = isUsing ? 7 : 6;  // Roach / Roach Burning
        }

        stack.set(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA,
                  new net.minecraft.world.item.component.CustomModelData(customModelData));
    }

    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.NONE; // No animation to avoid eating/drinking visuals
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack, @Nonnull LivingEntity entity) {
        return 32; // Same as food items
    }

    @Override
    @SuppressWarnings("null")
    public void onUseTick(@Nonnull Level level, @Nonnull LivingEntity entity, @Nonnull ItemStack stack, int remainingTicks) {
        // Spawn smoke particles while using (every few ticks)
        if (level instanceof ServerLevel serverLevel && remainingTicks % 4 == 0) {
            Vec3 lookVec = entity.getLookAngle();
            double x = entity.getX() + lookVec.x * 0.5;
            double y = entity.getEyeY() - 0.1;
            double z = entity.getZ() + lookVec.z * 0.5;

            // Spawn campfire smoke particles (slower rising smoke)
            serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    x, y, z, 1, 0.02, 0.02, 0.02, 0.01);
        }
    }

    @Override
    @SuppressWarnings("null")
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity) {
        if (entity instanceof Player player) {
            // Spawn smoke puff when finished
            if (level instanceof ServerLevel serverLevel) {
                Vec3 lookVec = player.getLookAngle();
                double x = player.getX() + lookVec.x * 0.5;
                double y = player.getEyeY() - 0.1;
                double z = player.getZ() + lookVec.z * 0.5;

                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        x, y, z, 5, 0.05, 0.05, 0.05, 0.02);
            }

            // Apply the effects (can be overridden by subclasses)
            applyEffects(player);

            // Damage the item; it will break after 8 uses
            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, player.getUsedItemHand() == InteractionHand.MAIN_HAND
                        ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            }

            // Update model data after use (no longer burning)
            updateCustomModelData(stack, false);
        }

        return stack;
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity, int timeLeft) {
        // When player stops using without finishing, reset to non-burning state
        updateCustomModelData(stack, false);
        super.releaseUsing(stack, level, entity, timeLeft);
    }

    /**
     * Override this method to customize the effects applied when the reefer is consumed.
     * Default behavior: Clears all effects and applies Slowness II for 30 seconds.
     *
     * @param player The player consuming the reefer
     */
    @SuppressWarnings("null")
    protected void applyEffects(Player player) {
        // Default: Clear all effects (like milk)
        player.removeAllEffects();

        // Apply stronger slowness (Slowness II for 30 seconds) WITHOUT particles
        // ambient=false, showParticles=false, showIcon=true
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 1, false, false, true));
    }
}
