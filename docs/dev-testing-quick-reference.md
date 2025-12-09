# Development Testing Quick Reference

## Setting Up the Datapack

After creating worlds, copy the testing datapack to all worlds:

```bash
./gradlew copyTestDatapack --no-configuration-cache
```

This copies the datapack to all existing worlds in `run/saves/*/datapacks/`.

## Loading the Datapack In-Game

After copying the datapack:

1. **Load your world**
2. **Reload datapacks** (in chat):
   ```
   /reload
   ```

The datapack is automatically enabled since it's in the world's datapacks folder!

## Available Functions

After enabling the datapack, use these commands:

### Quick Setup (All-in-one)
```
/function debug:quick
```
- Gives all mod items and ingredients
- Teleports you to spawn (0, 100, 0)
- Sets time to day
- Clears weather

### Individual Functions

**Get Items:**
```
/function debug:setup
```
Gives all mod items and crafting ingredients.

**Apply Test Effects:**
```
/function debug:test_effects
```
Applies negative effects (poison, wither, slowness, etc.) to test reefer clearing.

**Test Flight:**
```
/function debug:test_flight
```
Switches to survival mode to test energy drink flight.

**Test Hunger/MSG:**
```
/function debug:test_hunger
```
Applies hunger effect to test MSG food enhancement.

**Clean Up:**
```
/function debug:cleanup
```
Clears inventory and effects, returns to creative mode.

## Items Given by `/function debug:setup`

### Mod Items
- `badhabits:rolling_paper` (64)
- `badhabits:ground_grass` (64)
- `badhabits:reefer` (1)
- `badhabits:energy_drink_base` (1)
- `badhabits:red_energy_drink` (1)
- `badhabits:green_energy_drink` (1)
- `badhabits:msg` (1)
- `badhabits:tear_locker` (1)

### Crafting Ingredients
- Crafting Table
- Short Grass (64) - for Ground Grass
- Paper (64) - for Rolling Paper
- Slime Ball (64) - for Rolling Paper
- Iron Nugget (64) - for Energy Drink Base
- Sugar (64) - for Energy Drink Base & MSG
- Water Bottle (1) - for Energy Drink Base
- Red Dye (64) - for Red Energy Drink
- Leather (64) - for Red Energy Drink
- Green Dye (64) - for Green Energy Drink
- Rotten Flesh (64) - for Green Energy Drink
- Brown Mushroom (64) - for MSG
- Red Mushroom (64) - for MSG
- Gunpowder (64) - for MSG
- Ghast Tear (64) - for Tear Locker
- Heavy Core (1) - for Tear Locker

## Troubleshooting

**Datapack not showing up:**
```
/datapack list available
```
Should show `[file/testing]` in the list.

**Functions not working:**
Make sure you enabled and reloaded the datapack:
```
/datapack enable "file/testing"
/reload
```

**Tab completion:**
Type `/function debug:` and press TAB to see all available functions.
