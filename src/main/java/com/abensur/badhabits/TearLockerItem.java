package com.abensur.badhabits;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class TearLockerItem extends Item implements ICurioItem {
    public TearLockerItem(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("null")
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.badhabits.tear_locker.tooltip.description").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.badhabits.tear_locker.tooltip.keybind").withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(Component.translatable("item.badhabits.tear_locker.tooltip.effect").withStyle(ChatFormatting.AQUA));
        tooltipComponents.add(Component.translatable("item.badhabits.tear_locker.tooltip.tagline").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // Tick logic handled by TearLockerHandler
        ICurioItem.super.curioTick(slotContext, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
