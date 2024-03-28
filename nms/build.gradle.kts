plugins {
    id("io.papermc.paperweight.userdev") version "1.5.11" apply false
}

subprojects {
    plugins.apply("java-library")
    plugins.apply("io.papermc.paperweight.userdev")

    dependencies {
        compileOnly(project(":api"))
    }
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
    compileOnly(project(":api"))
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    subprojects.forEach { compileOnly(it) }
}