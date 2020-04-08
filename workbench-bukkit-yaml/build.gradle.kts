dependencies {
    compileOnly(project(":workbench-bukkit-common"))
    compileOnly(project(":workbench-bukkit-async"))
    compileOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.3.0")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.2.3")
    compileOnly(project.properties["minecraft.current.version"]!!)
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    testCompile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    testCompile(project(":workbench-bukkit-common"))
    testCompile(project(":workbench-bukkit-async"))
    testCompile(project.properties["minecraft.current.version"]!!)
    testCompile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.3.0")
    testCompile("com.fasterxml.jackson.core:jackson-databind:2.2.3")
}