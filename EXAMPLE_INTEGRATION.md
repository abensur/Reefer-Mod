# Example: Creating Custom Reefer Variants

Want to add your own herb that can be smoked? Just extend `ReeferItem`!

## Simple Example: Lavender Reefer

### 1. Create Your Custom Reefer Item

```java
package com.yourname.yourmod;

import com.abensur.badhabits.ReeferItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

public class LavenderReeferItem extends ReeferItem {
    public LavenderReeferItem(Properties properties) {
        super(properties);
    }

    @Override
    protected void applyEffects(Player player) {
        // Clear all effects (like milk)
        player.removeAllEffects();

        // Add custom effects
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0));
    }
}
```

### 2. Register Your Items

```java
@Mod(YourMod.MODID)
public class YourMod {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("yourmod");

    public static final DeferredItem<Item> LAVENDER = ITEMS.registerSimpleItem("lavender");
    public static final DeferredItem<Item> GROUND_LAVENDER = ITEMS.registerSimpleItem("ground_lavender");

    // Your custom reefer with 8 uses
    public static final DeferredItem<Item> LAVENDER_REEFER = ITEMS.register("lavender_reefer",
        () -> new LavenderReeferItem(new Item.Properties().durability(8)));

    public YourMod(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
```

### 3. Add Recipes

**ground_lavender.json** - Turn flowers into ground material:
```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [{"item": "yourmod:lavender"}],
  "result": {"item": "yourmod:ground_lavender", "count": 2}
}
```

**lavender_reefer.json** - Combine with rolling paper:
```json
{
  "type": "minecraft:crafting_shapeless",
  "ingredients": [
    {"item": "yourmod:ground_lavender"},
    {"tag": "badhabits:rolling_papers"}
  ],
  "result": {"item": "yourmod:lavender_reefer"}
}
```

### 4. Add Translations

```json
{
  "item.yourmod.lavender": "Lavender",
  "item.yourmod.ground_lavender": "Ground Lavender",
  "item.yourmod.lavender_reefer": "Lavender Reefer"
}
```

## That's It!

Your lavender reefer:
- Has 8 uses with durability bar
- Shows visual stages as it's used
- Clears all effects and applies Regeneration + Damage Resistance
- Works with any mod's rolling papers (via the `badhabits:rolling_papers` tag)

## More Examples

**Combat Reefer:**
```java
protected void applyEffects(Player player) {
    player.removeAllEffects();
    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 400, 1));
    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0));
}
```

**Mining Reefer:**
```java
protected void applyEffects(Player player) {
    player.removeAllEffects();
    player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 1200, 1));
    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0));
}
```

**Harmful Reefer** (no clearing, just debuffs):
```java
protected void applyEffects(Player player) {
    // Don't clear effects - just add bad ones!
    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 600, 0));
    player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0));
}
```
