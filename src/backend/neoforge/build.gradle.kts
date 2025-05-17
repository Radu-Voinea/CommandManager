@file:Suppress("UnstableApiUsage")

import Utils.Companion.toPascalCase

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("architectury-plugin")
    id("dev.architectury.loom")
    id("com.gradleup.shadow")
}

loom {
    silentMojangMappingsLicense()
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations.implementation{
    isCanBeResolved = true
    isCanBeConsumed = false
}

val (type, module, id) = Utils.getProjectMetadata(project.name)

dependencies {
    minecraft(Libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(Libs.parchment)
    })

    "neoForge"(Libs.neoforge)
    modImplementation(Libs.architectury.neoforge)

    api(project(":common"))
    compileOnlyApi(project(":backend.common", configuration = "namedElements"))
    implementation(project(":backend.common", configuration = "transformProductionNeoForge"))

    modCompileOnlyApi("net.kyori:adventure-platform-neoforge:6.4.0")
}

tasks {
    remapJar {
        archiveFileName = "CommandManager-NeoForge-${version}.jar"
    }

    shadowJar {
        archiveFileName = "CommandManager-NeoForge-${version}-all.jar"
        configurations = listOf(project.configurations.implementation.get())

        exclude("architectury.common.json")
    }

    remapJar {
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
        archiveClassifier.set(null as String?)
    }

    jar {
        archiveClassifier.set("dev")
    }

}

