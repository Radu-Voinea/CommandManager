@file:Suppress("UnstableApiUsage")

import Utils.Companion.toPascalCase
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.named

plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow")
}

architectury {
    common("fabric,neoforge".split(","))
}

loom {
    silentMojangMappingsLicense()
}

configurations.implementation{
    isCanBeResolved = true
}

dependencies {
    minecraft(Libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(Libs.parchment)
    })

    modCompileOnly(Libs.fabric.loader)
    modCompileOnlyApi(Libs.architectury.common)

    api(project(":command-manager.common"))
    modCompileOnlyApi("net.kyori:adventure-platform-mod-shared-fabric-repack:6.0.0")
}

tasks {
    shadowJar{
        archiveFileName = "CommandManager-Backend-Common-${version}-all.jar"
        configurations = listOf(project.configurations.implementation.get())
    }

    remapJar {
        archiveFileName = "CommandManager-Backend-Common-${version}.jar"
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }
}
