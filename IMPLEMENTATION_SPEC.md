# RFC: Reefer Mod - Rolling Paper and Reefer Items

**Status:** Draft
**Date:** December 6, 2025
**Author:** Reefer Mod Development Team
**Target Platform:** NeoForge 1.21.1 (Minecraft 1.21.1)

## Abstract

This RFC proposes the addition of two new consumable items to the Reefer mod: Rolling Paper (a crafting material) and Reefer (a consumable item with unique effects). The Reefer item provides a milk-like debuff clearing effect but with the trade-off of applying poison, and features a durability system with 4 uses per item.

## Motivation

The goal is to introduce a unique consumable mechanic that offers players an alternative to milk buckets for clearing status effects, with balanced gameplay through the addition of a poison effect and limited uses per item.

## Specification

### 1. Items

#### 1.1 Rolling Paper
- **Type:** Simple crafting material
- **Properties:** Standard item with no special attributes
- **Purpose:** Crafting ingredient for Reefer
- **Display Name:** "Rolling Paper"

#### 1.2 Reefer
- **Type:** Consumable food item with durability
- **Properties:**
  - Durability: 4 uses
  - Food properties: Always edible, 0 nutrition, 1.0 saturation modifier
  - Custom consumption behavior
- **Effects:**
  - Clears all active status effects (positive and negative)
  - Applies Poison I for 10 seconds (200 ticks)
  - Damages item by 1 durability point per use
  - Item breaks after 4 uses
- **Display Name:** "Reefer"

### 2. Crafting Recipes

#### 2.1 Rolling Paper Recipe
- **Type:** Shaped crafting
- **Pattern:** Horizontal arrangement
- **Ingredients:**
  - 1x Paper (`minecraft:paper`)
  - 1x Slime Ball (`minecraft:slime_ball`)
- **Output:** 4x Rolling Paper (`reefer:rolling_paper`)
- **Recipe ID:** `reefer:rolling_paper`

#### 2.2 Reefer Recipe
- **Type:** Shapeless crafting
- **Ingredients:**
  - 1x Short Grass (`minecraft:short_grass`)
  - 1x Rolling Paper (`reefer:rolling_paper`)
- **Output:** 1x Reefer (`reefer:reefer`)
- **Recipe ID:** `reefer:reefer`

### 3. Technical Implementation

#### 3.1 Java Classes

##### 3.1.1 ReeferItem.java
**Location:** `src/main/java/com/abensur/reefer/ReeferItem.java`

```java
package com.abensur.reefer;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ReeferItem extends Item {
    public ReeferItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            // Clear all active status effects
            player.removeAllEffects();

            // Apply Poison I for 10 seconds
            player.addEffect(new MobEffectInstance(
                MobEffects.POISON,
                200,  // 10 seconds (200 ticks)
                0     // Level I (amplifier 0)
            ));

            // Damage the item by 1 use
            stack.hurtAndBreak(1, player, p -> {
                // Called when item breaks (after 4 uses)
            });
        }

        return super.finishUsingItem(stack, level, entity);
    }
}
```

##### 3.1.2 Reefer.java Modifications
**Location:** `src/main/java/com/abensur/reefer/Reefer.java`

Add the following item registrations to the existing `DeferredRegister.Items ITEMS`:

```java
// Register Rolling Paper as a simple item
public static final DeferredItem<Item> ROLLING_PAPER = ITEMS.registerSimpleItem(
    "rolling_paper",
    new Item.Properties()
);

// Register Reefer with custom item class and properties
public static final DeferredItem<Item> REEFER = ITEMS.register(
    "reefer",
    () -> new ReeferItem(new Item.Properties()
        .durability(4)  // 4 uses before breaking
        .food(new FoodProperties.Builder()
            .alwaysEdible()  // Can be consumed regardless of hunger
            .nutrition(0)    // No hunger restoration
            .saturationModifier(1f)  // 1 saturation point
            .build())
    )
);
```

Add to the `EXAMPLE_TAB` creative mode tab in the `displayItems` method:

```java
output.accept(ROLLING_PAPER.get());
output.accept(REEFER.get());
```

#### 3.2 Recipe Files

##### 3.2.1 rolling_paper.json
**Location:** `src/main/resources/data/reefer/recipes/rolling_paper.json`

```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [
    "PS"
  ],
  "key": {
    "P": {
      "item": "minecraft:paper"
    },
    "S": {
      "item": "minecraft:slime_ball"
    }
  },
  "result": {
    "item": "reefer:rolling_paper",
    "count": 4
  }
}
```

##### 3.2.2 reefer.json
**Location:** `src/main/resources/data/reefer/recipes/reefer.json`

```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {
      "item": "minecraft:short_grass"
    },
    {
      "item": "reefer:rolling_paper"
    }
  ],
  "result": {
    "item": "reefer:reefer"
  }
}
```

#### 3.3 Localization

##### 3.3.1 en_us.json Modifications
**Location:** `src/main/resources/assets/reefer/lang/en_us.json`

Add the following translation keys:

```json
{
  "item.reefer.rolling_paper": "Rolling Paper",
  "item.reefer.reefer": "Reefer"
}
```

#### 3.4 Item Models

##### 3.4.1 rolling_paper.json
**Location:** `src/main/resources/assets/reefer/models/item/rolling_paper.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "reefer:item/rolling_paper"
  }
}
```

##### 3.4.2 reefer.json
**Location:** `src/main/resources/assets/reefer/models/item/reefer.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "reefer:item/reefer"
  }
}
```

#### 3.5 Textures

##### 3.5.1 Placeholder Textures

**Rolling Paper Texture:**
- **Location:** `src/main/resources/assets/reefer/textures/item/rolling_paper.png`
- **Format:** PNG, 16x16 pixels, 32-bit RGBA
- **Description:** White/off-white rectangular paper design, simple and minimal, pixelated Minecraft style

**Reefer Texture:**
- **Location:** `src/main/resources/assets/reefer/textures/item/reefer.png`
- **Format:** PNG, 16x16 pixels, 32-bit RGBA
- **Description:** Green/brown rolled cigarette-style item with visible paper wrapping at ends, pixelated Minecraft style

##### 3.5.2 Texture Specifications for AI Generation

- **Dimensions:** 16x16 pixels (standard Minecraft item texture)
- **Color Depth:** 32-bit RGBA with alpha channel for transparency
- **Style:** Pixelated, matching Minecraft's visual aesthetic
- **Rolling Paper Characteristics:**
  - Primary colors: White (#FFFFFF) to off-white (#F5F5DC)
  - Thin rectangular paper design
  - Minimal detail, simple form
- **Reefer Characteristics:**
  - Primary colors: Green (#228B22, #556B2F) and brown (#8B4513, #A0522D)
  - Rolled cigarette-style shape
  - Visible paper at both ends
  - Subtle shading for depth

### 4. Game Mechanics

#### 4.1 Effect Clearing Behavior
The Reefer item uses `player.removeAllEffects()` which clears:
- All positive status effects (e.g., Regeneration, Speed, Strength)
- All negative status effects (e.g., Poison, Weakness, Mining Fatigue)
- Works identically to drinking a milk bucket

#### 4.2 Poison Effect
- **Effect Type:** `MobEffects.POISON`
- **Duration:** 200 ticks (10 seconds at 20 ticks/second)
- **Amplifier:** 0 (Poison I)
- **Damage Rate:** 1 heart every 1.25 seconds (25 ticks)
- **Total Damage:** ~4 hearts over 10 seconds (can be fatal if player has low health)

#### 4.3 Durability System
- Initial durability: 4 uses
- Each consumption reduces durability by 1
- After 4 uses, item breaks and is removed from inventory
- Durability bar displays remaining uses
- Cannot be repaired

### 5. Balance Considerations

#### 5.1 Advantages vs Milk Bucket
- Stackable (unlike milk buckets)
- Multiple uses per item (4 uses)
- Does not require cows or access to milk
- Provides small saturation benefit (1 point)

#### 5.2 Disadvantages vs Milk Bucket
- Applies poison effect (4 hearts damage over 10 seconds)
- Requires crafting materials (grass and paper/slime)
- Limited uses (breaks after 4 consumptions)
- Cannot remove poison if it's the only effect (will reapply it)

#### 5.3 Use Cases
- Emergency debuff removal in combat (when milk is unavailable)
- Clearing mining fatigue before respawning Elder Guardian effect
- Removing levitation before fall damage
- Clearing slowness/weakness in PvP situations
- Risk vs reward: clear dangerous debuffs but take poison damage

### 6. Creative Mode Integration

Both items are added to the mod's custom creative mode tab (`EXAMPLE_TAB`), which appears after the Combat tab in the creative inventory.

### 7. Compatibility

#### 7.1 NeoForge API Requirements
- **NeoForge Version:** 21.1.216
- **Minecraft Version:** 1.21.1
- **Java Version:** 21

#### 7.2 Required Imports
```java
// Core Minecraft imports
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// NeoForge registration
import net.neoforged.neoforge.registries.DeferredItem;
```

### 8. Testing Checklist

- [ ] Rolling Paper crafts correctly from paper + slime ball
- [ ] Reefer crafts correctly from short grass + rolling paper
- [ ] Reefer is consumable regardless of hunger level
- [ ] Reefer clears all active status effects on consumption
- [ ] Reefer applies Poison I for exactly 10 seconds
- [ ] Reefer durability reduces by 1 per use
- [ ] Reefer breaks after 4 uses
- [ ] Items appear in creative mode tab
- [ ] Items have correct display names
- [ ] Textures render correctly in inventory
- [ ] Textures render correctly when held
- [ ] Textures render correctly when dropped
- [ ] Recipe appears in recipe book
- [ ] Works correctly in multiplayer
- [ ] Works correctly on dedicated servers

### 9. Future Enhancements

Potential features for future versions:
- Custom particle effects when consuming Reefer
- Custom sound effects for consumption
- Achievement/advancement for crafting or using Reefer
- Variants with different effects (different herbs)
- Integration with other mods (e.g., Farmer's Delight)
- Custom durability bar colors to indicate remaining uses
- Tooltip showing remaining uses
- Config options for poison duration/strength
- Config option to disable/enable the feature

### 10. Implementation Order

1. Create `ReeferItem.java` class
2. Modify `Reefer.java` to register items
3. Create recipe JSON files
4. Add translations to `en_us.json`
5. Create item model JSON files
6. Create placeholder texture files
7. Test in development environment
8. Replace placeholder textures with final art assets
9. Final testing and bug fixes
10. Documentation and release

## Backwards Compatibility

This is a new feature addition with no breaking changes to existing functionality. No migration or upgrade path is required.

## Security Considerations

- Effect clearing uses Minecraft's built-in `removeAllEffects()` method
- Poison effect uses vanilla `MobEffects.POISON`
- No custom network packets or commands are introduced
- No file I/O or external resource access
- Standard NeoForge item registration patterns are used

## References

- [NeoForge Documentation](https://docs.neoforged.net/)
- [Minecraft Wiki - Status Effects](https://minecraft.wiki/w/Status_effect)
- [Minecraft Wiki - Poison](https://minecraft.wiki/w/Poison)
- [Minecraft Wiki - Food](https://minecraft.wiki/w/Food)
- [NeoForge Item Registration](https://docs.neoforged.net/docs/items/)

## Changelog

- **2025-12-06:** Initial RFC draft created

---

**Status:** Awaiting implementation
**Next Steps:** Begin implementation with ReeferItem.java class creation
