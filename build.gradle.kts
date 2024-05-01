plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7" apply false
}

allprojects {
    plugins.apply("java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }

    java {
        toolchain.languageVersion = JavaLanguageVersion.of(21)
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.release = 21
            dependsOn(clean)
        }
    }
}