
# Bad Habits

A Minecraft mod that adds consumable items with meaningful tradeoffs and interesting mechanics.

## Features

### Reefer
- **Rolling Paper**: Craftable with paper and slime ball (makes 32)
- **Ground Grass**: Process short grass into ground material
- **Reefer**: Consumable item that clears all status effects but applies slowness
- **Durability System**: Each reefer has 8 uses with visual progression
- **Particle Effects**: Smoke particles while using

### Energy Drink
- **Emergency Flight**: Grants 10 seconds of creative flight
- **High Cost**: Drains 5-6 hunger points + Hunger III during flight
- **Energy Crash**: Weakness II & Mining Fatigue II for 30 seconds after
- **3 Uses**: Non-stackable with durability
- **Recipe**: Iron nuggets, redstone, and sugar

### Dragon Builder Amulet
- **Beast Template**: Craftable meal plan made from armadillo scutes, baked potato, and cooked chicken
- **Smithing Upgrade**: Combine the template with a chain and the dragon egg to forge the amulet
- **Permanent Buff**: Grants passive Strength II and Haste II when carried or equipped
- **Clean Fuel**: Zero drawbacks—this replaces the jittery drink with pure Beast Mode discipline

### Artificial Seasoning (Flavor Enhancer)
- **Food Doubler**: Next food eaten restores double hunger and saturation
- **30 Second Buff**: Use within 30 seconds or it expires
- **5 Uses**: Non-stackable with durability
- **Recipe**: White/Yellow/Red dyes plus a clay ball and paper

## Design Philosophy

All items in Bad Habits follow the same principle: **meaningful tradeoffs**. Each item is powerful but comes with significant costs, making them situational emergency tools rather than routine-use items. This maintains game balance while adding interesting tactical decisions.

## For Mod Developers

Want to add your own consumables? Items can easily be extended:

```java
// Example: Custom reefer variant
public class LavenderReeferItem extends ReeferItem {
    public LavenderReeferItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Player player) {
        player.removeAllEffects();
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
    }
}
```

Package: `com.abensur.badhabits`

See **[EXAMPLE_INTEGRATION.md](EXAMPLE_INTEGRATION.md)** for complete examples!

## Installation

This mod requires NeoForge 1.21.1.

Development Setup:
1. Clone this repository
2. Run `gradlew build` to build the mod
3. Run `gradlew runClient` to test in game

Mapping Names:
============
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

Additional Resources:
==========
Community Documentation: https://docs.neoforged.net/
NeoForged Discord: https://discord.neoforged.net/
