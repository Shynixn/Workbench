import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version ("2.0.4")
}

tasks.withType<ShadowJar> {
    archiveName = "$baseName-$version.$extension"
}

publishing {
    publications {
        (findByName("mavenJava") as MavenPublication).artifact(tasks.findByName("shadowJar")!!)
    }
}

dependencies {
    implementation(project(":workbench-bukkit:workbench-bukkit-v1_15_R1"))
    compileOnly("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.3.0")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.2.3")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    compileOnly(project.properties["minecraft.current.version"]!!)
    testCompile(project.properties["minecraft.current.version"]!!)
    testCompile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")
    testCompile("com.fasterxml.jackson.core:jackson-databind:2.2.3")
    testCompile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.3.0")

}