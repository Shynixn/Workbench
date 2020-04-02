# PetBlocks WorkBench-Bukkit-Async

WorkBench-Bukkit-Common is a library to simplify common bukkit operations.

## Required dependencies

```xml
implementation("com.github.shynixn.workbench:workbench-bukkit-common:0.0.+")
implementation("com.github.shynixn.workbench:workbench-minecraft-common:0.0.+")
implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.71")
```

## Demos

```kotlin
log {
    error { "Connection failed!" }
    throwable { e }
}
```

```kotlin
player {
    "Shynixn"
}
```

```kotlin
findClazz("net.minecraft.server.VERSION.ItemStack")
```

```kotlin
player.sendMessage(red() + "Hello World!")
```

```kotlin
location {
    x = 1.0
    y = 2.0
    z = 3.0
    yaw = 4.0
    pitch = 5.0
}
```