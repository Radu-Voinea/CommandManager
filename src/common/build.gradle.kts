dependencies {
    // Dependencies
    if (project.properties["com.raduvoinea.utils.local"] != null) {
        api(project(project.properties["com.raduvoinea.utils.local"] as String))
    } else {
        api(libs.raduvoinea.utils)
    }
    api(libs.luckperms)
    api(libs.kyori.minimessage)

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
