@file:Suppress("UnstableApiUsage")

import Utils.Companion.toPascalCase
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.named

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("architectury-plugin")
    id("dev.architectury.loom")
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

configurations.implementation{
    isCanBeResolved = true
    isCanBeConsumed = false
}

val (type, module, id) = Utils.getProjectMetadata(project.name)

loom {
    silentMojangMappingsLicense()
}

dependencies {
    minecraft(Libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(Libs.parchment)
    })

    modImplementation(Libs.fabric.loader)
    modApi(Libs.fabric.api)
    modApi(Libs.architectury.fabric)

    api(project(":command-manager-common"))
    compileOnlyApi(project(":command-manager-backend-common", configuration = "namedElements"))
    implementation(project(":command-manager-backend-common", configuration = "transformProductionFabric"))

    modCompileOnlyApi("net.kyori:adventure-platform-fabric:6.4.0")
}

tasks {
    shadowJar {
        archiveFileName = "CommandManager-Fabric-${version}-all.jar"

        configurations = listOf(project.configurations.implementation.get())
        exclude("architectury.common.json")
    }

    remapJar {
        archiveFileName = "CommandManager-Fabric-${version}.jar"
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }

    jar {
        archiveClassifier.set("dev")
    }
}

components.getByName("java") {
    this as AdhocComponentWithVariants
    this.withVariantsFromConfiguration(project.configurations["shadowRuntimeElements"]) {
        skip()
    }
}