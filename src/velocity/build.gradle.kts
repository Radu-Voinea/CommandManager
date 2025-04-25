@file:Suppress("VulnerableLibrariesLocal")

plugins{
    id("org.jetbrains.gradle.plugin.idea-ext")
}

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

tasks {
    sourceSets.main {
        java {
            srcDir("$buildDir/generated/sources/templates")
        }
    }

    compileJava {
        dependsOn(generateTemplates)
    }

}

val generateTemplates = tasks.register<Sync>("generateTemplates") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    println("$projectDir")

    from("$projectDir/src/main/templates")
    into("$buildDir/generated/sources/templates")

    filter { line ->
        line.replace("\${id}", project.name.lowercase().replace(".", "_"))
            .replace("\${version}", project.version as String)
            .replace("{id}", project.name.lowercase().replace(".", "_"))
            .replace("{version}", project.version as String)
    }
}

