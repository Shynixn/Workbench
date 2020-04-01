dependencies {
    compileOnly(project(":workbench-bukkit-common"))
    compileOnly(project(":workbench-minecraft-common"))
    compileOnly(project.properties["minecraft.current.version"]!!)
}