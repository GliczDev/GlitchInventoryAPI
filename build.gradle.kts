plugins {
    id("java")
}

allprojects {
    plugins.apply("java")

    repositories {
        mavenCentral()
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
            dependsOn(clean)
        }
    }
}