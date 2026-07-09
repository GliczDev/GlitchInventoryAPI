plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.21" apply false
}

subprojects {
    afterEvaluate {
        dependencies {
            "compileOnly"(project(":api"))
            "compileOnly"("org.projectlombok:lombok:1.18.46")
            "annotationProcessor"("org.projectlombok:lombok:1.18.46")
        }
    }
}
