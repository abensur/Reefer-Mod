package com.abensur.reefer;

import java.util.List;
import javax.annotation.Nonnull;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class GroundGrassItem extends Item {
    public GroundGrassItem(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("null")
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context, @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.reefer.ground_grass.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
