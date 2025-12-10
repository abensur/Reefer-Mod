package com.abensur.badhabits;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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

    // Artificial Seasoning consumable - doubles next food restoration
    public static final DeferredItem<Item> ARTIFICIAL_SEASONING = ITEMS.register("artificial_seasoning",
            () -> new ArtificialSeasoningItem(new Item.Properties().durability(5)));

    // Empty Tear Locker block item
    public static final DeferredItem<Item> EMPTY_TEAR_LOCKER_BLOCK_ITEM = ITEMS.register("empty_tear_locker",
        () -> new EmptyTearLockerBlockItem(ModBlocks.EMPTY_TEAR_LOCKER.get(), new Item.Properties()));

    // Tear Locker - endgame curio for clearing negative effects
    public static final DeferredItem<Item> TEAR_LOCKER = ITEMS.register("tear_locker",
            () -> new TearLockerItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // Endless Curry Pouch - endgame curio for doubling food nutrition
    public static final DeferredItem<Item> ENDLESS_CURRY_POUCH = ITEMS.register("endless_curry_pouch",
            () -> new CurryPouchItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // Icarus's Wing - permanent creative flight toggle
    public static final DeferredItem<Item> ICARUS_WING = ITEMS.register("icarus_wing",
            () -> new IcarusWingItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // Beast Template - smithing template for the Dragon Builder Amulet
    public static final DeferredItem<Item> BEAST_TEMPLATE = ITEMS.register("beast_template",
            () -> new BeastTemplateItem(new Item.Properties()));

    // Dragon Builder Amulet - curio replacement for the green energy drink
    public static final DeferredItem<Item> DRAGON_BUILDER_AMULET = ITEMS.register("dragon_builder_amulet",
            () -> new DragonBuilderAmuletItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // Attachment type for tracking flight duration
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Long>> FLIGHT_END_TIME = ATTACHMENTS.register(
        "flight_end_time",
        () -> AttachmentType.builder(() -> 0L)
            .serialize(Codec.LONG)
            .build()
    );

    // Attachment type for tracking Artificial Seasoning buff duration
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Long>> ARTIFICIAL_SEASONING_BUFF_END_TIME = ATTACHMENTS.register(
        "artificial_seasoning_buff_end_time",
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

    // Attachment to track whether creative flight is currently active from the wing
    @SuppressWarnings("null")
    public static final Supplier<AttachmentType<Boolean>> ICARUS_WING_FLIGHT = ATTACHMENTS.register(
        "icarus_wing_flight",
        () -> AttachmentType.builder(() -> Boolean.FALSE)
            .serialize(Codec.BOOL)
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
                output.accept(EMPTY_TEAR_LOCKER_BLOCK_ITEM.get());
                output.accept(ENERGY_DRINK_BASE.get());
                output.accept(RED_ENERGY_DRINK.get());
                output.accept(GREEN_ENERGY_DRINK.get());
                output.accept(ARTIFICIAL_SEASONING.get());
                output.accept(TEAR_LOCKER.get());
                output.accept(ENDLESS_CURRY_POUCH.get());
                output.accept(ICARUS_WING.get());
                output.accept(BEAST_TEMPLATE.get());
                output.accept(DRAGON_BUILDER_AMULET.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public BadHabits(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register data generation
        modEventBus.addListener(this::gatherData);

        // Register blocks
        ModBlocks.BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so attachments get registered
        ATTACHMENTS.register(modEventBus);

        // Register energy drink handler for tick events
        EnergyDrinkHandler.register(modEventBus);

        // Register Artificial Seasoning handler for food events
        ArtificialSeasoningHandler.register(modEventBus);

        // Register Tear Locker handler
        TearLockerHandler.register(modEventBus);

        // Register Endless Curry Pouch handler
        CurryPouchHandler.register(modEventBus);

        // Register Icarus Wing handler
        IcarusWingHandler.register(modEventBus);

        // Register Dragon Builder Amulet handler
        DragonBuilderAmuletHandler.register(modEventBus);

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
