rootProject.name = "GlitchInventoryAPI"

include(
    "api",
    "core",
    "nms",
    "plugin"
)

listOf(
    "1_20_6",
    "1_21_5",
    "26_1"
).forEach {
    val name = ":nms:nms-v$it"

    include(name)
    project(name).projectDir = file("nms/$it")
}
