import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

val nms = project(":nms")

val reobf: Configuration by configurations.creating
val mojMap: Configuration by configurations.creating

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation(project(":api"))

    implementation(nms)
    nms.subprojects.forEach {
        reobf(project(":nms:${it.name}", "reobf"))
        mojMap(it)
    }
}

java {
    withSourcesJar()
}

tasks {
    withType<ShadowJar> {
        group = "shadow"

        from(sourceSets.main.get().output)
        from(sourceSets.main.get().runtimeClasspath)

        archiveBaseName = rootProject.name
    }
}

task<ShadowJar>("shadowJarReobf") {
    from(reobf)

    archiveClassifier = null
}

task<ShadowJar>("shadowJarMojMap") {
    from(mojMap)

    archiveClassifier = "mojmap"
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
            artifactId = "${rootProject.name.lowercase()}-${project.name.lowercase()}"

            artifact(tasks.named("sourcesJar"))
            artifact(tasks.named("shadowJarReobf"))
            artifact(tasks.named("shadowJarMojMap"))
        }
    }
}