package com.abensur.reefer;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.data.event.GatherDataEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Reefer.MODID)
public class Reefer {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "reefer";
    // Create a Deferred Register to hold Items which will all be registered under the "reefer" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "reefer" namespace
    @SuppressWarnings("null")
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Rolling paper crafting component
    public static final DeferredItem<Item> ROLLING_PAPER = ITEMS.register("rolling_paper",
            () -> new RollingPaperItem(new Item.Properties()));

    // Ground grass - processed ingredient for reefer
    public static final DeferredItem<Item> GROUND_GRASS = ITEMS.register("ground_grass",
            () -> new GroundGrassItem(new Item.Properties()));

    // Reefer consumable with custom behavior and durability
    public static final DeferredItem<Item> REEFER = ITEMS.register("reefer",
            () -> new ReeferItem(new Item.Properties().durability(8)));

    // Creates a creative tab with the id "reefer:reefer_tab" for mod items
    @SuppressWarnings("null")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> REEFER_TAB = CREATIVE_MODE_TABS.register("reefer_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.reefer"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ROLLING_PAPER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ROLLING_PAPER.get());
                output.accept(GROUND_GRASS.get());
                output.accept(REEFER.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Reefer(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register data generation
        modEventBus.addListener(this::gatherData);

        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Common setup
    }

    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ReeferRecipeProvider(packOutput, lookupProvider));
    }

}
