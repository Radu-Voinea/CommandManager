@file:Suppress("VulnerableLibrariesLocal")

dependencies {
    // Velocity
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    // Project
    api(project(":command-manager-common"))

    // Dependencies
    if (project.properties["com.raduvoinea.utils.local"] != null) {
        api(project(project.properties["com.raduvoinea.utils.local"] as String))
    } else {
        api(libs.raduvoinea.utils)
    }

    // Annotations
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    compileOnly(libs.jetbrains.annotations)
    annotationProcessor(libs.jetbrains.annotations)
    testCompileOnly(libs.jetbrains.annotations)
    testAnnotationProcessor(libs.jetbrains.annotations)
}

