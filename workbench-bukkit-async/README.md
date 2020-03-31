# PetBlocks WorkBench-Bukkit-Async

WorkBench-Bukkit-Async is a library to simplify async and sync operations. 

## Required dependencies

```xml
implementation("com.github.shynixn.workbench:workbench-bukkit-async:0.0.+")
implementation("com.github.shynixn.workbench:workbench-bukkit-common:0.0.+")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.71")
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
async {
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

    