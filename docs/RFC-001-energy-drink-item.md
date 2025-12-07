# RFC-001: Energy Drink Item Implementation

**Status:** Approved
**Author:** Bad Habits Mod Team
**Date:** 2025-12-07
**Version:** 1.0

---

## Summary

This RFC proposes the implementation of a new consumable item called "Energy Drink" for the Bad Habits Mod. The item provides temporary creative flight (10 seconds) as an early-game mobility solution, balanced by significant post-effect debuffs to ensure situational, emergency-only usage.

---

## Motivation

The Bad Habits Mod received positive community feedback with requests to expand beyond the existing reefer item. The goal is to:

1. Add new consumable items with meaningful tradeoffs
2. Maintain a **neutral, non-incentivizing approach** (no substance glorification)
3. Keep the mod scope small and manageable (first mod project)
4. Respect Minecraft/Mojang Terms of Service
5. Follow the established pattern: emergency items with costs, not routine-use items

---

## Design Philosophy

### Core Principles

1. **Neutrality**: Items are situational tools, not addictive mechanics
2. **Emergency Use**: Solutions for specific problems, not constant use
3. **Tradeoff**: Benefits always come with costs
4. **No Incentive**: Avoid glorification of any substances
5. **ToS Compliance**: Generic items without explicit harmful substance references

### Existing Pattern (Reefer Item)

- ✅ Removes all negative effects (like milk)
- ❌ Applies Slowness I for 10 seconds
- ❌ **Does NOT restore hunger/saturation**
- 📊 Result: **Neutral emergency item** (useful but not ideal)

The Energy Drink follows this same philosophy.

---

## Detailed Design

### Item Concept

**Energy Drink**: Generic energy beverage (no explicit branding) that grants temporary creative flight for early-game aerial mobility access, with heavy post-use penalties.

### Mechanics

#### During Flight (10 seconds = 200 ticks)
- ✈️ **Creative Flight** active
- Full directional control (like creative mode)
- Activate with double-space (standard creative flight)
- No negative effects during flight

#### Immediate Effects (upon consumption)
- 🍖 **-5 to -6 hunger points** (exhaustion: 6.0F)
- 🔥 **Hunger III** for 10 seconds (200 ticks) - accelerated metabolism

#### Post-Flight Crash (30 seconds each)
- 💪 **Weakness II** for 30 seconds (600 ticks) - extreme muscle fatigue
- ⛏️ **Mining Fatigue II** for 30 seconds (600 ticks) - concentration difficulty

#### Audio/Visual Feedback
- ✨ END_ROD particles on activation (15 particles)
- 💨 SMOKE particles on deactivation/crash (25 particles)
- 🔊 BEACON_ACTIVATE sound on activation (pitch 1.5)
- 🔊 BEACON_DEACTIVATE sound on crash (pitch 0.8)

### Real-World Energy Drink Effects (Reference)

The debuff design is based on actual energy drink side effects:
- Tachycardia (rapid heartbeat) → Hunger (accelerated metabolism)
- Jitters/tremors → (considered but omitted for gameplay comfort)
- Energy crash (extreme fatigue) → Weakness II
- Dehydration → Hunger points consumption
- Difficulty concentrating → Mining Fatigue II

---

## Crafting Recipe

```
[Iron Nugget]  [Redstone]     [Iron Nugget]
[Iron Nugget]  [Sugar]        [Iron Nugget]
[Iron Nugget]  [Sugar]        [Iron Nugget]
```

**Ingredients:**
- 6x Iron Nugget
- 1x Redstone
- 2x Sugar

**Output:** 1x Energy Drink

**Rationale:**
- **Iron Nugget** = aluminum can (thematic + accessible)
- **Redstone** = "electrical energy" component
- **Sugar** = sweet energizing liquid
- **Accessibility**: Available from early game
- **Balance**: Cheap to craft, but heavy debuffs prevent spam

---

## Technical Implementation

### File Structure

```
src/main/java/com/abensur/badhabits/
├── EnergyDrinkItem.java (NEW)
├── EnergyDrinkHandler.java (NEW - tick event handler)
└── [Main mod file] (MODIFY - register attachments)

src/main/resources/
├── assets/badhabits/
│   ├── textures/item/
│   │   └── energy_drink.png (NEW)
│   ├── models/item/
│   │   └── energy_drink.json (NEW - if needed)
│   └── lang/
│       └── en_us.json (MODIFY - add translations)
└── data/badhabits/
    └── recipes/
        └── energy_drink.json (NEW)
```

### Implementation Approach: Player Data Attachment

Using NeoForge's Attachment system to track flight timer:

```java
// 1. Create Attachment Type for timestamp storage
public static final Supplier<AttachmentType<Long>> FLIGHT_END_TIME = ATTACHMENTS.register(
    "flight_end_time",
    () -> AttachmentType.builder(() -> 0L)
        .serialize(Codec.LONG)
        .build()
);

// 2. In finishUsingItem(), activate flight and store timestamp
@Override
public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
    if (entity instanceof Player player && !level.isClientSide()) {
        // Activate Creative Flight
        player.getAbilities().mayfly = true;
        player.getAbilities().flying = true;
        player.onUpdateAbilities();

        // Store when flight should end (gameTime + duration)
        long endTime = level.getGameTime() + 200; // 10 seconds = 200 ticks
        player.setData(FLIGHT_END_TIME, endTime);

        // Apply immediate effects
        player.getFoodData().addExhaustion(6.0F); // -5 to -6 hunger
        player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 200, 2)); // Hunger III for 10s

        // Activation particles
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.END_ROD,
                player.getX(), player.getY() + 1, player.getZ(),
                15, 0.3, 0.5, 0.3, 0.08);
        }

        // Activation sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
            SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 0.5F, 1.5F);
    }
    return stack;
}

// 3. Register ServerTickEvent to check and deactivate flight
public class EnergyDrinkHandler {
    public static void register(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener(EnergyDrinkHandler::onServerTick);
    }

    private static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.hasData(FLIGHT_END_TIME)) {
                long endTime = player.getData(FLIGHT_END_TIME);

                if (endTime > 0 && server.overworld().getGameTime() >= endTime) {
                    // Deactivate flight (only if not creative)
                    if (!player.isCreative() && !player.isSpectator()) {
                        player.getAbilities().mayfly = false;
                        player.getAbilities().flying = false;
                        player.onUpdateAbilities();

                        // Apply energy crash - FINAL EFFECTS
                        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 1)); // Weakness II for 30s
                        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 600, 1)); // Mining Fatigue II for 30s

                        // Crash particles
                        ServerLevel level = (ServerLevel) player.level();
                        level.sendParticles(ParticleTypes.SMOKE,
                            player.getX(), player.getY() + 1, player.getZ(),
                            25, 0.3, 0.5, 0.3, 0.03);

                        // Deactivation/crash sound
                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.BEACON_DEACTIVATE, SoundSource.PLAYERS, 0.5F, 0.8F);
                    }

                    // Reset timestamp
                    player.setData(FLIGHT_END_TIME, 0L);
                }
            }
        }
    }
}
```

### Item Class Details

**EnergyDrinkItem.java**
- Extend `Item` (not `ReeferItem` - no inheritance needed)
- Override `finishUsingItem()` - apply effects and activate flight
- Override `use()` - initiate consumption
- Override `getUseDuration()` - return 32 (same as food)
- Override `getUseAnimation()` - return `UseAnim.DRINK`
- Override `appendHoverText()` - explanatory tooltip

---

## Balance Analysis

### Why 10 Seconds Works

- Sufficient time for significant traversal (200-300 blocks)
- Allows complete tactical repositioning
- Provides early-game access to elytra-like mobility
- Generous duration balanced by heavy crash penalties

### Why Weakness II + Mining Fatigue II

- **Weakness II**: -6 damage (nearly useless in combat for 30s)
- **Mining Fatigue II**: 90% slower mining
- Combined: Player is **extremely vulnerable** post-use
- Guarantees emergency-only usage, prevents spam

### Design Philosophy Maintained

- ✅ Accessible (cheap recipe)
- ✅ Powerful (10s creative flight)
- ✅ High cost (30s nearly defenseless)
- ✅ Emergency item (not worth routine use)

---

## Use Cases

### ✅ Good For:
- Escaping mob hordes (fly away, endure crash in safety)
- Crossing impossible ravine/canyon
- Reaching mountaintop quickly
- Cave exploration with deadly gaps
- Emergency in lava/imminent danger

### ❌ Bad For:
- Routine travel (crash leaves you vulnerable)
- Combat (Weakness II is deadly)
- Mining (Mining Fatigue II disables this)
- Use near mobs (crash = certain death)

---

## Comparison with Alternatives

| Item | Mobility | Cost | When to Use |
|------|----------|------|-------------|
| **Energy Drink** | 10s creative flight | 30s heavy crash | Emergency/exploration |
| **Elytra** | Infinite gliding | Firework + durability | Routine travel |
| **Ender Pearl** | Instant teleport | 2.5 hearts damage | Combat/quick escape |
| **Jump Boost Potion** | High jumps | No debuff | Light mobility |

The Energy Drink fills the niche of **"disposable emergency elytra"** - more powerful than pearls but with a high price.

---

## Scope & Limitations

### What to Do ✅
- Keep code simple and readable
- Use existing Minecraft mechanics
- Focus on **situational** consumables
- Maximum **2-3 items** initially
- Maintain neutral, responsible approach
- Items should be **emergency tools**, not routine

### What NOT to Do ❌
- **Explicit references to illegal/harmful substances**
- **Glorification or encouragement of substance use**
- Complex systems (addiction, withdrawal mechanics)
- New blocks or structures (keep simple)
- Custom mobs
- Complex crafting systems
- Mod integration (initially)
- Items that are "always better" (avoid forced meta)

---

## Assets Required

### Textures
- `energy_drink.png` (16x16 pixels)
- Design: Generic can (vibrant colors - blue/red/green)
- Avoid real brand logos or replicas

### Style
- Simple, clean pixel art
- Colors distinct from reefer for easy identification
- No real brand replication

### Tools
- Use existing `generate_textures.py` script (adapt for can)
- Or create manually in pixel art editor

---

## Implementation Timeline

**Estimated: 2-3 hours total**

1. Create `EnergyDrinkItem.java` class (30 min)
2. Create `EnergyDrinkHandler.java` tick handler (30 min)
3. Register item and attachments (15 min)
4. Create texture `energy_drink.png` (30 min - 1h)
5. Create crafting recipe JSON (15 min)
6. Add translations (10 min)
7. In-game testing and balance adjustments (30 min - 1h)

---

## Testing Plan

1. **Functional Testing**
   - Verify flight activation
   - Verify 10-second timer accuracy
   - Verify crash debuffs apply correctly
   - Verify creative/spectator mode protection
   - Verify particles and sounds trigger

2. **Balance Testing**
   - Test in combat scenarios
   - Test in mining scenarios
   - Test in exploration scenarios
   - Verify debuffs are punishing enough to prevent spam
   - Verify flight duration is useful but not overpowered

3. **Edge Cases**
   - Player dies during flight
   - Player logs out during flight
   - Player changes dimensions during flight
   - Multiple players using simultaneously

---

## Future Considerations

### Version 1.1 (This RFC)
- **Energy Drink only** (single new item)
- Simple crafting recipe
- Basic balance testing
- No unnecessary refactoring

### Future Versions (If Well-Received)
- Evaluate community feedback
- Consider other **generic, neutral** items
- Examples: espresso coffee, soda, etc.
- Always maintain emergency/tradeoff approach

### Possible Refactoring (3+ Items)
- If adding 3+ similar items, consider abstract base class
- Current approach: YAGNI (You Aren't Gonna Need It) - only 2 items don't justify abstraction

---

## Community Feedback Strategy

### Questions for Community (Reddit)
1. Does the 10-second flight seem balanced?
2. Is the hunger cost (-5 to -6 points) fair?
3. Are the 30-second crash debuffs too harsh/lenient?
4. Should Energy Drink be craftable or structure loot?
5. Suggestions for other **generic, neutral** items (no glorification)?

### Where to Post
- r/feedthebeast
- r/MinecraftMods
- CurseForge Forums

### Important Disclaimer
When posting, make clear:
- Mod maintains neutral, responsible approach
- Items are situational/emergency, not encouraged for routine use
- Respects Minecraft/Mojang ToS

---

## Alternatives Considered

### Creative Flight vs Levitation

| Aspect | Levitation | Creative Flight |
|--------|-----------|-----------------|
| Control | ❌ Only goes up | ✅ Full 3D control |
| Speed | 🐌 Slow | ⚡ Fast |
| Stop mid-air | ❌ Keeps rising | ✅ Can hover |
| Feeling | Strange/limited | 🎮 Like creative mode |
| Use Case | Vertical only | Free flight |

**Decision: Creative Flight is MUCH better for the proposal!**

### Flight Duration Options

- **4 seconds**: Too short, frustrating
- **7 seconds**: Good for most emergencies, balanced
- **10 seconds** ⭐: Generous, significant traversal, requires heavy debuffs (chosen)
- **15+ seconds**: Too powerful, would need even heavier penalties

---

## Risks & Mitigations

### Risk: Item Too Powerful
**Mitigation:** Heavy 30-second crash debuffs (Weakness II + Mining Fatigue II) make routine use unviable

### Risk: Item Too Weak
**Mitigation:** 10 seconds is generous; can adjust debuff duration in future updates based on feedback

### Risk: Timer Issues (Death/Logout)
**Mitigation:** Timer stored in player attachment; resets on death (acceptable - player loses flight)

### Risk: Performance (ServerTickEvent)
**Mitigation:** Only checks players with active attachment; minimal overhead

### Risk: ToS Violation
**Mitigation:** Generic "energy drink" with no explicit harmful substance references; neutral approach

---

## Success Criteria

1. ✅ Item craftable and consumable
2. ✅ 10-second creative flight functions correctly
3. ✅ Crash debuffs apply as specified
4. ✅ No game-breaking bugs or exploits
5. ✅ Community feedback is positive or constructive
6. ✅ No ToS or policy violations
7. ✅ Code is clean, maintainable, and well-documented

---

## Acceptance Criteria

- [ ] `EnergyDrinkItem.java` implemented and registered
- [ ] `EnergyDrinkHandler.java` tick handler implemented
- [ ] Attachment type registered for flight timer
- [ ] Crafting recipe JSON created
- [ ] Texture `energy_drink.png` created
- [ ] Translations added to `en_us.json`
- [ ] All functional tests pass
- [ ] Balance testing completed
- [ ] Edge cases handled
- [ ] Code reviewed and approved
- [ ] Documentation updated

---

## Conclusion

This RFC proposes a carefully balanced, thematically appropriate, and technically sound addition to the Reefer Mod. The Energy Drink item:

- Maintains the mod's **neutral, responsible philosophy**
- Provides **meaningful early-game utility** (creative flight)
- Includes **significant tradeoffs** (heavy crash debuffs)
- Keeps **scope small and manageable** (single item, ~3 hours work)
- **Respects Minecraft ToS** (generic item, no substance glorification)
- Follows **established pattern** (emergency item with costs)

The implementation is straightforward using NeoForge's Attachment system and ServerTickEvent, with clear success criteria and testing plan.

**Recommendation: Approve and implement.**

---

## References

- Reefer Mod existing codebase
- NeoForge Documentation: Attachments, Events, Player Abilities
- Minecraft Wiki: Potion Effects, Creative Mode
- Community feedback threads (Reddit: r/feedthebeast, r/MinecraftMods)

---

**Next Steps:**
1. Approve this RFC
2. Create feature branch: `feature/energy-drink-item`
3. Implement according to specification
4. Test thoroughly
5. Submit for code review
6. Merge to main and release v1.1
