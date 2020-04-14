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
    implementation(project(":workbench-bukkit-common:workbench-bukkit-common-v1_15_R1"))
    implementation(project(":workbench-minecraft-common"))
    compileOnly(project(":workbench-minecraft-common"))
    compileOnly(project.properties["minecraft.current.version"]!!)
}