plugins {
    id("maven-publish")
    id("com.gradleup.shadow")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation(project(":api"))
    project.project(":nms").subprojects.forEach {
        implementation(it)
    }
}

tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang+yarn"
    }

    archiveClassifier = null
}

val sourcesJarTask = tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allSource)
    from(project(":api").sourceSets.main.get().allSource)
    archiveClassifier = "sources"
}

publishing {
    repositories {
        val repoType = if (version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"
        maven("https://repo.roxymc.net/${repoType}") {
            name = "roxymc"
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        create<MavenPublication>("maven") {
            artifactId = "inventoryapi-${project.name.lowercase()}"

            artifact(sourcesJarTask)

            from(components["shadow"])
        }
    }
}
