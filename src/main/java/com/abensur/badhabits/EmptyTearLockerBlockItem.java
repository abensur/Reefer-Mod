package com.abensur.badhabits;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public class EmptyTearLockerBlockItem extends BlockItem {
    public EmptyTearLockerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("block.badhabits.empty_tear_locker.tooltip.usage").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("block.badhabits.empty_tear_locker.tooltip.warning").withStyle(ChatFormatting.DARK_RED));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
