@file:Suppress("UnstableApiUsage")

plugins {
    id(libs.plugins.loom.get().pluginId) version libs.plugins.loom.get().version.toString()
}

dependencies {
    // Minecraft
    minecraft(libs.minecraft.v20)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(libs.parchment.mappings)
    })

    modCompileOnly(libs.fabric.loader)
    modCompileOnly(libs.fabric.api)

    // Project
    api(project(":command-manager-common"))

    // Dependencies
    if (project.properties["com.voinearadu.utils.local"] != null) {
        api(project(project.properties["com.voinearadu.utils.local"] as String))
    } else {
        api("com.voinearadu:utils:1.1.6")
    }
    api(libs.luckperms)
    modApi(libs.kyori.adventure.fabric)

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
    jar {
        archiveFileName = "FabricCommandManager-$version.jar"
    }

    processResources {
        inputs.property("version", version)

        filesMatching("fabric.mod.json") {
            expand("version" to version)
        }
    }

    remapJar {
        archiveFileName = "FabricCommandManager-$version.jar"
    }
}