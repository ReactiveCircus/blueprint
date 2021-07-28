rootProject.name = "Blueprint"

include(":blueprint-interactor-common")
include(":blueprint-interactor-coroutines")
include(":blueprint-interactor-rx2")
include(":blueprint-interactor-rx3")
include(":blueprint-async-coroutines")
include(":blueprint-async-rx2")
include(":blueprint-async-rx3")
include(":blueprint-ui")
include(":blueprint-testing-robot")
includeProject(":demo-coroutines", "samples/demo-coroutines")
includeProject(":demo-rx", "samples/demo-rx")
includeProject(":demo-common", "samples/demo-common")
includeProject(":demo-testing-common", "samples/demo-testing-common")
include(":test-utils")

fun includeProject(name: String, filePath: String) {
    include(name)
    project(name).projectDir = File(filePath)
}

enableFeaturePreview("VERSION_CATALOGS")
