package com.abensur.badhabits;

import com.mojang.serialization.Codec;
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
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BadHabits.MODID)
public class BadHabits {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "badhabits";
    // Create a Deferred Register to hold Items which will all be registered under the "badhabits" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "badhabits" namespace
    @SuppressWarnings("null")
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    // Create a Deferred Register to hold AttachmentTypes which will all be registered under the "reefer" namespace
    @SuppressWarnings("null")
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    // Rolling paper crafting component
    public static final DeferredItem<Item> ROLLING_PAPER = ITEMS.register("rolling_paper",
            () -> new RollingPaperItem(new Item.Properties()));

    // Ground grass - processed ingredient for reefer
    public static final DeferredItem<Item> GROUND_GRASS = ITEMS.register("ground_grass",
            () -> new GroundGrassItem(new Item.Properties()));

    // Reefer consumable with custom behavior and durability
    public static final DeferredItem<Item> REEFER = ITEMS.register("reefer",
            () -> new ReeferItem(new Item.Properties().durability(8)));

    // Energy Drink Base - crafting ingredient (not consumable)
    public static final DeferredItem<Item> ENERGY_DRINK_BASE = ITEMS.register("energy_drink_base",
            () -> new EnergyDrinkBaseItem(new Item.Properties()));

    // Red Energy Drink - grants temporary flight with debuffs
    public static final DeferredItem<Item> RED_ENERGY_DRINK = ITEMS.register("red_energy_drink",
            () -> new RedEnergyDrinkItem(new Item.Properties().durability(3)));

    // Green Energy Drink - grants haste and jump boost with debuffs
    public static final DeferredItem<Item> GREEN_ENERGY_DRINK = ITEMS.register("green_energy_drink",
            () -> new GreenEnergyDrinkItem(new Item.Properties().durability(3)));

    // MSG consumable - doubles next food restoration
    public static final DeferredItem<Item> MSG = ITEMS.register("msg",
            () -> new MSGItem(new Item.Properties().durability(3)));

    // Tear Locker - endgame curio for clearing negative effects
    public static final DeferredItem<Item> TEAR_LOCKER = ITEMS.register("tear_locker",
            () -> new TearLockerItem(new Item.Properties().stacksTo(1)));

    // Attachment type for tracking flight duration
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Long>> FLIGHT_END_TIME = ATTACHMENTS.register(
        "flight_end_time",
        () -> AttachmentType.builder(() -> 0L)
            .serialize(Codec.LONG)
            .build()
    );

    // Attachment type for tracking MSG buff duration
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Long>> MSG_BUFF_END_TIME = ATTACHMENTS.register(
        "msg_buff_end_time",
        () -> AttachmentType.builder(() -> 0L)
            .serialize(Codec.LONG)
            .build()
    );

    // Attachment type for tracking green energy drink beast mode duration
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Long>> BEAST_MODE_END_TIME = ATTACHMENTS.register(
        "beast_mode_end_time",
        () -> AttachmentType.builder(() -> 0L)
            .serialize(Codec.LONG)
            .build()
    );

    // Attachment type for tracking tear locker cooldown
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Long>> TEAR_LOCKER_COOLDOWN = ATTACHMENTS.register(
        "tear_locker_cooldown",
        () -> AttachmentType.builder(() -> 0L)
            .serialize(Codec.LONG)
            .build()
    );

    // Attachment type for tracking tear locker immunity end time
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Long>> TEAR_LOCKER_IMMUNITY_END = ATTACHMENTS.register(
        "tear_locker_immunity_end",
        () -> AttachmentType.builder(() -> 0L)
            .serialize(Codec.LONG)
            .build()
    );

    // Creates a creative tab with the id "badhabits:bad_habits_tab" for mod items
    @SuppressWarnings("null")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> REEFER_TAB = CREATIVE_MODE_TABS.register("bad_habits_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.badhabits"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ROLLING_PAPER.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ROLLING_PAPER.get());
                output.accept(GROUND_GRASS.get());
                output.accept(REEFER.get());
                output.accept(ENERGY_DRINK_BASE.get());
                output.accept(RED_ENERGY_DRINK.get());
                output.accept(GREEN_ENERGY_DRINK.get());
                output.accept(MSG.get());
                output.accept(TEAR_LOCKER.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public BadHabits(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register data generation
        modEventBus.addListener(this::gatherData);

        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so attachments get registered
        ATTACHMENTS.register(modEventBus);

        // Register energy drink handler for tick events
        EnergyDrinkHandler.register(modEventBus);

        // Register MSG handler for food events
        MSGHandler.register(modEventBus);

        // Register Tear Locker handler
        TearLockerHandler.register(modEventBus);

        // Register network packets
        modEventBus.addListener(this::registerPayloads);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Common setup
    }

    @SuppressWarnings("null")
    private void registerPayloads(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");
        registrar.playToServer(
            ActivateTearLockerPacket.TYPE,
            ActivateTearLockerPacket.STREAM_CODEC,
            ActivateTearLockerPacket::handle
        );
    }

    private void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new BadHabitsRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), new BadHabitsCuriosProvider(MODID, packOutput, existingFileHelper, lookupProvider));

        // Register item tags provider for Curios slot compatibility
        generator.addProvider(event.includeServer(),
            new BadHabitsItemTagsProvider(packOutput, lookupProvider,
                java.util.concurrent.CompletableFuture.completedFuture(net.minecraft.data.tags.TagsProvider.TagLookup.empty()),
                existingFileHelper));
    }

}
