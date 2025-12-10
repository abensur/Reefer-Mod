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

public class CurryPouchItem extends Item implements ICurioItem {
    public CurryPouchItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.badhabits.endless_curry_pouch.tooltip.effect").withStyle(ChatFormatting.GREEN));
        tooltipComponents.add(Component.translatable("item.badhabits.endless_curry_pouch.tooltip.info").withStyle(ChatFormatting.YELLOW));
        tooltipComponents.add(Component.translatable("item.badhabits.endless_curry_pouch.tooltip.tagline").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        // Tick logic handled by CurryPouchHandler
        ICurioItem.super.curioTick(slotContext, stack);
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }
}
