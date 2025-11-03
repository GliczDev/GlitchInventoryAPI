plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    implementation(project(":core", "shadow"))
}

tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang+yarn"
    }

    archiveBaseName = "${rootProject.name}-${project.name}"
    archiveClassifier = null
}

bukkit {
    name = rootProject.name
    main = "me.glicz.inventoryapi.plugin.GlitchInventoryAPIPlugin"
    author = "Glicz"
    apiVersion = "1.20"
}
