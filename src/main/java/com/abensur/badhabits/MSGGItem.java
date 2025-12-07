package com.abensur.badhabits;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class MSGGItem extends Item {
    public MSGGItem(Properties properties) {
        super(properties);
    }

    @Override
    public net.minecraft.sounds.SoundEvent getBreakingSound() {
        return SoundEvents.ITEM_BREAK;
    }

    @Override
    @SuppressWarnings("null")
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.badhabits.msgg.tooltip.effect").withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(Component.translatable("item.badhabits.msgg.tooltip.duration").withStyle(ChatFormatting.GRAY));

        // Add durability/uses information
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        int usesRemaining = maxDamage - damage;
        tooltipComponents.add(Component.translatable("item.badhabits.msgg.tooltip.uses", usesRemaining, maxDamage).withStyle(ChatFormatting.GRAY));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    @SuppressWarnings("null")
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack, @Nonnull LivingEntity entity) {
        return 32; // Same as food items
    }

    @Override
    @SuppressWarnings("null")
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity) {
        if (entity instanceof Player player && !level.isClientSide()) {
            // Store when MSGG buff should end (gameTime + duration)
            long endTime = level.getGameTime() + 600; // 30 seconds = 600 ticks
            player.setData(BadHabits.MSGG_BUFF_END_TIME, endTime);

            // Activation particles (golden/yellow for food enhancement)
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.WAX_ON,
                    player.getX(), player.getY() + 1, player.getZ(),
                    15, 0.3, 0.5, 0.3, 0.08);
            }

            // Activation sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, 1.2F);

            // Damage the item; it will break after 3 uses
            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, player.getUsedItemHand() == InteractionHand.MAIN_HAND
                        ? net.minecraft.world.entity.EquipmentSlot.MAINHAND : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
            }
        }

        return stack;
    }
}
