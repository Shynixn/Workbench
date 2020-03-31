dependencies {
    compileOnly(project(":workbench-minecraft-common"))
    compileOnly(project.properties["minecraft.current.version"]!!)
}