plugins {
    id("maven-publish")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
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

            from(components["java"])
        }
    }
}