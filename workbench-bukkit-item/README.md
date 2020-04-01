# PetBlocks WorkBench-Bukkit-Item

WorkBench-Bukkit-Item is a library to simplify common item operations.

## Required dependencies

```xml
implementation("com.github.shynixn.workbench:workbench-bukkit-item:0.0.+")
implementation("com.github.shynixn.workbench:workbench-bukkit-common:0.0.+")
implementation("com.github.shynixn.workbench:workbench-minecraft-common:0.0.+")
implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.71")
```

## Demos

```kotlin
item {
    name = "Stone"
    dataValue = 1
    displayName = "My stone block"
    lore = listOf("This is my stone block.")
}
```

```kotlin
item {
    name = "Stone"
    dataValue = 1
    displayName = "My stone block"
    lore = listOf("This is my stone block.")
}.matches(item {
    name = "Wood"
    dataValue = 0
    displayName = "My wooden block"
    lore = listOf("This is my wooden block.")
}, Item.name, Item.dataValue, Item.displayName, Item.lore)
```

