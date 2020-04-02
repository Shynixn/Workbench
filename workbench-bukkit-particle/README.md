# PetBlocks WorkBench-Bukkit-Particle

WorkBench-Bukkit-Particle is a library to simplify common particle operations.

## Required dependencies

```xml
implementation("com.github.shynixn.workbench:workbench-bukkit-particle:0.0.+")
implementation("com.github.shynixn.workbench:workbench-bukkit-async:0.0.+")
implementation("com.github.shynixn.workbench:workbench-bukkit-item:0.0.+")
implementation("com.github.shynixn.workbench:workbench-bukkit-common:0.0.+")
implementation("com.github.shynixn.workbench:workbench-minecraft-common:0.0.+")
implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.71")
```

## Demos

```kotlin
particle {
    name = "REDDUST"
    colorRed = 255
}.play(location, player)
```