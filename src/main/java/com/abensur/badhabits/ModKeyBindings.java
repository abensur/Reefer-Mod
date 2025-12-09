package com.abensur.badhabits;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = BadHabits.MODID, value = Dist.CLIENT)
public class ModKeyBindings {
    public static final String KEY_CATEGORY = "key.categories." + BadHabits.MODID;
    public static final String KEY_ACTIVATE_TEAR_LOCKER = "key." + BadHabits.MODID + ".activate_tear_locker";

    public static final Lazy<KeyMapping> ACTIVATE_TEAR_LOCKER = Lazy.of(() -> new KeyMapping(
            KEY_ACTIVATE_TEAR_LOCKER,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V, // Default to 'V' key
            KEY_CATEGORY
    ));

    @SubscribeEvent
    @SuppressWarnings("null")
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ACTIVATE_TEAR_LOCKER.get());
    }
}
