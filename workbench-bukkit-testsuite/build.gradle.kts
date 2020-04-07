import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version ("2.0.4")
}

tasks.withType<ShadowJar> {
    archiveName = "$baseName-$version.$extension"

    // Change the output folder of the plugin.
    destinationDir = File("D:\\Benutzer\\Temp\\plugins")

    relocate("kotlin", "com.github.shynixn.workbench.lib.kotlin")

    relocate("org.intellij", "com.github.shynixn.workbench.lib.org.intelli")
    relocate("org.jetbrains", "com.github.shynixn.workbench.lib.org.jetbrains")
    relocate("org.bstats", "com.github.shynixn.workbench.lib.org.bstats")
    relocate("javax.inject", "com.github.shynixn.workbench.lib.javax.inject")
    relocate("org.aopalliance", "com.github.shynixn.workbench.lib.org.aopalliance")
    relocate("org.slf4j", "com.github.shynixn.workbench.lib.org.slf4j")

    relocate("com.google", "com.github.shynixn.workbench.lib.com.google")
    relocate("com.zaxxer", "com.github.shynixn.workbench.lib.com.zaxxer")
    relocate("org.apache", "com.github.shynixn.workbench.lib.org.apache")
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":workbench-bukkit-async"))
    implementation(project(":workbench-bukkit-common"))
    implementation(project(":workbench-bukkit-item"))
    implementation(project(":workbench-bukkit-particle"))
    implementation(project(":workbench-minecraft-common"))
    compileOnly(project.properties["minecraft.current.version"]!!)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.71")
}