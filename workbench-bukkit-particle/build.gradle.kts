dependencies {
    compileOnly(project(":workbench-bukkit-async"))
    compileOnly(project(":workbench-bukkit-item"))
    compileOnly(project(":workbench-bukkit-common"))
    compileOnly(project(":workbench-minecraft-common"))
    compileOnly(project.properties["minecraft.current.version"]!!)
}