plugins {
    id("io.papermc.paperweight.userdev")
}

dependencies {
    paperweight.paperDevBundle("26.1.1.build.+")
    compileOnly(project(":nms:nms-v1_20_6"))
    compileOnly(project(":nms:nms-v1_21_5"))
}
