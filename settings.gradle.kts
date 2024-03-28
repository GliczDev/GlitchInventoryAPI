rootProject.name = "GlitchInventoryAPI"
include(
    "api",
    "core"
)
include("nms")
include("nms:v1_20_R3")
findProject(":nms:v1_20_R3")?.name = "v1_20_R3"
include("plugin")
