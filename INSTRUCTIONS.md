# Instructions for LLM Contributions

These notes explain how to extend **Bad Habits** without breaking the existing patterns. When in doubt about behavior, naming, or balance, ask the user before guessing—tradeoffs (and their eventual clean replacements) are a core part of the mod’s identity.

## Understand the Baseline

- **Mod identity:** Tactical consumables with meaningful crashes that push the player to seek healthier, endgame curios. Each “bad habit” grants a strong effect alongside a cost, and every habit eventually has a counterpart that offers the same benefit with no drawback once the player “kicks” the habit (see `README.md` and `docs/RFC-001-energy-drink-item.md`).
- **Tech stack:** NeoForge 21, Java 21, Curios API. Main namespace is `badhabits`. Java sources live in `src/main/java/com/abensur/badhabits`.
- **Generated data:** Recipes, Curios tags, and advancements live in `src/generated/resources`; they are produced by `./gradlew runData` via `BadHabitsRecipeProvider`, `BadHabitsCuriosProvider`, and `BadHabitsItemTagsProvider`.
- **Localization:** Both `en_us.json` and `pt_br.json` must stay in sync.

### Habit Progression Template

| Early “bad habit” | Crash | Clean endgame counterpart | Drawback-free effect |
| --- | --- | --- | --- |
| `Reefer` | Slowness II after cleanse | `Tear Locker` curio | On-demand cleanse + 5s immunity via hotkey |
| `Artificial Seasoning` | Buff expires if unused; durability | `Endless Curry Pouch` | Passive food doubling forever |
| `Red Energy Drink` | Flight timer ends with Weakness II | `Icarus's Wing` | Permanent creative flight toggle |
| `Green Energy Drink` | Mining Fatigue II crash | `Dragon Builder Amulet` | Constant Strength II + Haste II |

When adding new systems, keep the same arc: introduce a consumable with obvious costs, and plan (or at least reserve room for) its sober counterpart that preserves the benefit as a Curio/curio-like item with no crashes.

**Sizing & Release Pacing**

- Ship the consumable first (or alongside its curio) but never leave a habit without a clearly defined upgrade path. If the upgrade is not ready for the same release, describe the plan in `docs/` and reserve crafting hooks (e.g., special drops, templates).
- Curios must feel like endgame discipline: use rare recipes (dragon egg, heavy cores, enchanted items, templates, etc.) so the clean path has a meaningful cost beyond the consumable’s crash.
- Crashes should be at least as punishing as the permanent version is powerful. E.g., if the curio grants permanent Strength II, the consumable should inflict a Mining Fatigue, slowness, or hunger penalty that lasts long enough to discourage repeated spam.

## Quick Checklist for a New Item

1. **Pitch the tradeoff.** Describe the “bad habit” consumable’s benefit and crash, plus the long-term curio that replaces it once the player moves on.
2. **Create the Java item class** (or extend an existing base such as `ReeferItem`, `EnergyDrinkBaseItem`, or implement `ICurioItem` for accessories). For a new habit, that means one consumable class and (eventually) one Curio/passive item.
3. **Register the item(s)** in `BadHabits.java` (use `ITEMS.register`) and add them to the creative tab output list, keeping consumables and curios grouped.
4. **Add handlers/attachments** if the habit tracks timers/crashes or if the curio needs persistent buffs (see examples under “Advanced behavior” below).
5. **Define crafting/loot data** inside `BadHabitsRecipeProvider` so `runData` can generate JSON under `src/generated`.
6. **Provide assets:** 16×16 PNG under `src/main/resources/assets/badhabits/textures/item`, `models/item/<id>.json`, and (if needed) custom model overrides.
7. **Add translations** (name + tooltips) to both language files. Tooltips should call out both the “boost” and the “crash,” and the curio’s story should echo the idea of kicking the habit.
8. **Update docs/testing tools** if QA commands or `docs/dev-testing-quick-reference.md` need the new item pair, and document when the curio becomes available (crafting unlock, boss drop, etc.).
9. **Re-run generators/tests:** `./gradlew runData`, then `./gradlew build` or `./gradlew runClient` as appropriate.

## Java Implementation Patterns

- **Naming:** Use lowercase snake_case IDs (`endless_curry_pouch`). The translation key will then be `item.badhabits.endless_curry_pouch`.
- **Consumables with durability:** Set `.durability(uses)` and show remaining uses in tooltips like `ReeferItem`, `RedEnergyDrinkItem`, and `ArtificialSeasoningItem`. Use `Component.translatable("item.badhabits.<id>.tooltip.*")` so localization stays clean.
- **Reefer-style variants:** Extend `ReeferItem` and override `applyEffects(Player)` to change buffs/debuffs. The base class already handles smoke particles, durability, and the `CustomModelData` stages defined in `assets/badhabits/models/item/reefer*.json`.
- **Curios or passive items:** Implement `ICurioItem`, add explanatory tooltip text, and keep unique logic in a handler (e.g., `DragonBuilderAmuletHandler`, `CurryPouchHandler`). Inventory checks plus Curios slots allow items to work even if Curios isn’t installed. These curios are always the “sober” upgrades that grant the same benefits without the crash.
- **Handlers:** Each complex item has a `*Handler` registered from the `BadHabits` constructor via `NeoForge.EVENT_BUS`. Follow the same structure (static `register`, then private event listeners).

### Advanced Behavior Patterns

| Need | Use |
| --- | --- |
| Track timers, cooldowns, toggles | Register a `AttachmentType` in `BadHabits.ATTACHMENTS` (see `FLIGHT_END_TIME`, `ARTIFICIAL_SEASONING_BUFF_END_TIME`, etc.) and read/write it via `player.setData/getData`. |
| Server tick processing | Create a handler like `EnergyDrinkHandler` or `ArtificialSeasoningHandler` and register it to listen for `ServerTickEvent`/`PlayerTickEvent`. |
| Inventory-based equips | Combine Curios checks (`CuriosApi.getCuriosInventory`) with vanilla inventory searches so behavior still works if Curios is missing. |
| Keybind-triggered abilities | Mirror the Tear Locker flow: key mapping (`ModKeyBindings`), client tick listener (`ClientKeyInputHandler`), packet (`ActivateTearLockerPacket`), and server-side handler (`TearLockerHandler.activate`). |
| Multi-stage models | Use `CustomModelData` predicates plus additional model files (see `reefer_*.json`) and set the data in code (see `ReeferItem#updateCustomModelData`). |

## Registration & Data

- **Item registration:** Add a new `DeferredItem<Item>` (or subclass) to `BadHabits.java`. Keep related registrations grouped (consumables together, curios together). Also append `output.accept(...)` in `REEFER_TAB` so players can access it from the Bad Habits creative tab.
- **Blocks:** If a new block item is required (like `EmptyTearLockerBlockItem`), register the `Block` in `ModBlocks` and the `BlockItem` in `BadHabits`.
- **Recipes & advancements:** Update `BadHabitsRecipeProvider.buildRecipes`. The provider automatically creates recipe advancements; no manual JSON edits—run `./gradlew runData` and commit the refreshed files under `src/generated/resources/data/badhabits/`.
- **Curios tags:** Extend `BadHabitsItemTagsProvider` so Curios knows which slot accepts the new item. The generator writes to `src/generated/resources/data/curios/tags/item/*.json`.
- **Additional data:** If you introduce new tags, loot tables, or advancements beyond recipes, add providers here as well so the data pipeline stays single-sourced.

## Assets & Localization

- **Textures:** Place 16×16 PNGs under `assets/badhabits/textures/item`. If you’re updating reefer visuals, the helper scripts in `textures/` can regenerate staged PNGs.
- **Models:** Add `assets/badhabits/models/item/<id>.json` pointing to the texture. For durability-driven variants, add override files similar to the reefer set.
- **Blockstates/models:** Blocks use `assets/badhabits/blockstates` plus `models/block`. Follow the `empty_tear_locker` example for overlays.
- **Localization:** Update both `en_us.json` and `pt_br.json`, keeping tooltip structure consistent (`tooltip.boost`, `tooltip.crash`, `tooltip.uses`, etc.). Tooltips should describe both the upside and the drawback.
- **Creative tab name/description:** If you add whole new systems, consider refreshing `README.md` and `docs` to describe them.

## Testing Workflow & Tools

- **Dev world helpers:** The datapack under `data/minecraft/datapacks/testing` exposes `/function debug:*` commands (`setup`, `quick`, `test_effects`, `test_hunger`, `test_flight`, `cleanup`). Update `setup.mcfunction` (and its sub-functions) so both the consumable and the curio appear in QA kits (e.g., `debug:setup/<habit>.mcfunction`).
- **Copying datapack:** Run `./gradlew copyTestDatapack --no-configuration-cache` before testing so every world gets the latest functions (the task runs automatically before `runClient`, but it’s safe to trigger manually).
- **Smoke tests:** Launch `./gradlew runClient`, use `/function debug:quick`, and verify the new item’s boost and crash mechanics, sounds, and particles.
- **Regression:** Ensure Tear Locker hotkey (`V` by default) still works, attachments are reset, and Curios fallbacks behave when Curios is missing.

### Habit Pair QA Checklist

- [ ] Consumable added to `debug:setup` so testers can spam the crash.
- [ ] Curio added to `debug:setup` (or sub-command) and verified in **survival** with Curios installed and absent (inventory fallback must keep the effect working).
- [ ] Crash timers verified: habit causes the intended drawback every time, even with latency or stacking multiple consumptions.
- [ ] Curio gating verified: crafting or drop requirements match the “endgame” story (e.g., dragon egg, templates, boss drops).
- [ ] Documentation touched (`README.md`, `docs/dev-testing-quick-reference.md`, etc.) to explain when the clean upgrade is available.

## Reference Commands

```
./gradlew runData             # Regenerate recipes/tags/advancements into src/generated/resources
./gradlew copyTestDatapack    # Sync debug datapack into all run/saves worlds
./gradlew runClient           # Manual in-game testing
./gradlew build               # Full compile/test before committing
```

## Finishing Touches

- Keep comments high-level and only where behavior is non-obvious (follow the tone in existing files).
- Verify git status before finishing; do not revert user changes.
- If you introduce a new interaction model (e.g., another on-demand curio), add a short note to `docs/dev-testing-quick-reference.md` so other devs know how to test it.
- Always prefer asking clarifying questions over assumptions when requirements feel incomplete.
