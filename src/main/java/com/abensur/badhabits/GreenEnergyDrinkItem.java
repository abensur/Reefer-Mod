package com.abensur.badhabits;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class GreenEnergyDrinkItem extends Item {
    public GreenEnergyDrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public net.minecraft.sounds.SoundEvent getBreakingSound() {
        return SoundEvents.ITEM_BREAK;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.badhabits.green_energy_drink.tooltip.boost").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.translatable("item.badhabits.green_energy_drink.tooltip.crash").withStyle(ChatFormatting.RED));

        // Add durability/uses information
        int maxDamage = stack.getMaxDamage();
        int damage = stack.getDamageValue();
        int usesRemaining = maxDamage - damage;
        tooltipComponents.add(Component.translatable("item.badhabits.green_energy_drink.tooltip.uses", usesRemaining, maxDamage).withStyle(ChatFormatting.DARK_GRAY));

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
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack, @Nonnull LivingEntity entity) {
        return 32; // Same as food items
    }

    @Override
    @SuppressWarnings("null")
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity) {
        if (entity instanceof Player player && !level.isClientSide()) {
            // "Unleash the Beast" - Positive effects for 2 minutes (2400 ticks)
            // Strength II - increased attack damage (beast power)
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2400, 1, false, false, true));
            // Speed II - fast movement (beast speed)
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2400, 1, false, false, true));

            // Store when beast mode should end (gameTime + 2400 ticks)
            long endTime = level.getGameTime() + 2400;
            player.setData(BadHabits.BEAST_MODE_END_TIME, endTime);

            // Play burp sound
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1.0F, 1.0F);

            // Damage the item (3 uses)
            if (!player.isCreative()) {
                stack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
            }
        }

        return stack;
    }
}
