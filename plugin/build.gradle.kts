import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18-R0.1-SNAPSHOT")
    compileOnly(project(":api"))
    compileOnly(project(":core"))
}

tasks {
    withType<ShadowJar> {
        group = "shadow"

        from(sourceSets.main.get().output)
        from(sourceSets.main.get().runtimeClasspath)

        val coreJarTask = project(":core").tasks.named(name)
        dependsOn(coreJarTask)
        from(coreJarTask)

        archiveBaseName = "${rootProject.name}-${project.name}"
    }
}

task<ShadowJar>("shadowJarReobf") {
    archiveClassifier = null
}

task<ShadowJar>("shadowJarMojMap") {
    archiveClassifier = "mojmap"
}

bukkit {
    name = rootProject.name
    main = "me.glicz.inventoryapi.plugin.GlitchInventoryAPIPlugin"
    author = "Glicz"
    apiVersion = "1.18"
}