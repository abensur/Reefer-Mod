
# Reefer Mod

A Minecraft mod that adds rolling papers and consumable reefers with unique effects.

## Features

- **Rolling Paper**: Craftable with paper and slime ball (makes 32)
- **Ground Grass**: Process short grass into ground material
- **Reefer**: Consumable item that clears status effects but applies slowness
- **Durability System**: Each reefer has 8 uses with visual progression
- **Particle Effects**: Smoke particles while using

## For Mod Developers

Want to add your own smokeable herbs? Just extend `ReeferItem` and override one method!

```java
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
