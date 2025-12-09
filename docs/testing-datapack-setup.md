# Setting Up Testing Datapacks for Minecraft Mods

## 1. Create the Datapack Structure

```bash
# In your mod's root directory
mkdir -p data/minecraft/datapacks/testing/data/debug/function
```


## 2. Create pack.mcmeta

Create `data/minecraft/datapacks/testing/pack.mcmeta`:

```json
{
  "pack": {
    "pack_format": 48,
    "description": "Testing utilities for your mod"
  }
}
```

## 3. Create Function Files

Create `.mcfunction` files in `data/debug/function/`:

### setup.mcfunction

Gives all items needed for testing:

```mcfunction
give @s yourmod:item1 64
give @s yourmod:item2 1
give @s minecraft:crafting_table 1
give @s minecraft:ingredient1 64
give @s minecraft:ingredient2 64
gamemode creative
say §a[Testing] Items loaded!
```

### effects.mcfunction

Apply effects for testing specific features:

```mcfunction
effect give @s minecraft:poison 30 1
effect give @s minecraft:wither 30 1
say §e[Testing] Effects applied!
```

### cleanup.mcfunction

Reset everything:

```mcfunction
clear @s
effect clear @s
gamemode creative
say §c[Testing] Reset complete!
```

### quick.mcfunction

Combined setup with teleport and world settings:

```mcfunction
function debug:setup
tp @s 0 100 0
time set day
weather clear
say §a[Testing] Quick setup complete!
```

## 4. Load the Datapack in Dev Environment

### Option A: Use Gradle Task (Recommended for Dev)

Add this to your `build.gradle`:

```gradle
// Copy testing datapack to run directory for all new worlds
task copyTestDatapack(type: Copy) {
    from 'data/minecraft/datapacks/testing'
    into 'run/datapacks/testing'
}

// Run before launching the client
tasks.named('runClient').configure {
    dependsOn copyTestDatapack
}
```

Now the datapack will be automatically available when you run `./gradlew runClient`.

To enable it in a world:
1. Create a new world
2. Open chat and run: `/datapack enable "file/testing"`
3. Run: `/reload`

### Option B: Manual Copy

```bash
# For existing worlds
cp -r data/minecraft/datapacks/testing run/saves/"YourWorld"/datapacks/

# Or copy to run/datapacks for new worlds to find
mkdir -p run/datapacks
cp -r data/minecraft/datapacks/testing run/datapacks/
```

## 5. Add to .gitignore

Keep testing datapack local to your dev environment:

```bash
echo "data/minecraft/datapacks/testing/" >> .gitignore
```

## 6. Use In-Game

After loading the world, use these commands:

- `/function debug:setup` - Get all items
- `/function debug:effects` - Apply test effects
- `/function debug:cleanup` - Reset everything
- `/function debug:quick` - Full setup + teleport

## Project Structure

```
your-mod/
├── data/
│   └── minecraft/
│       └── datapacks/
│           └── testing/
│               ├── pack.mcmeta
│               └── data/
│                   └── debug/              # Namespace used in commands
│                       └── function/
│                           ├── setup.mcfunction
│                           ├── effects.mcfunction
│                           ├── cleanup.mcfunction
│                           └── quick.mcfunction
└── run/
    └── saves/
        └── YourWorld/
            └── datapacks/
                └── testing/ (copied here)
```
