# PetBlocks WorkBench-Bukkit-Async

WorkBench-Bukkit-Async is a library to simplify async and sync operations. 

## Required dependencies

```xml
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
```

## Demos

```kotlin
launch {
    println("Hello")
    delay(1000)
    println("World")
}
```

```kotlin
@EventHandler
fun onPlayerJoinEvent(event: PlayerJoinEvent) = launch {
}
```

    