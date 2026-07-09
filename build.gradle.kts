plugins {
    id("com.gradleup.shadow") version "8.3.6" apply false
}

configure(subprojects.filter { it.name != "nms" }) {
    plugins.apply("java")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion = JavaLanguageVersion.of(25)
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.release = 25
            dependsOn("clean")
        }
    }
}
