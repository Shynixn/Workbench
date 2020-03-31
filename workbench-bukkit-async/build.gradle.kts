dependencies {
    compileOnly(project(":workbench-bukkit-common"))
    compileOnly(project.properties["minecraft.current.version"]!!)
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
}