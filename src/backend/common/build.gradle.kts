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

dependencies {
    minecraft(Libs.minecraft)
    mappings(loom.layered {
        officialMojangMappings()
        parchment(Libs.parchment)
    })

    modCompileOnly(Libs.fabric.loader)
    modCompileOnly(Libs.architectury.common)

    api(project(":command-manager-common"))
    modCompileOnly("net.kyori:adventure-platform-mod-shared-fabric-repack:6.0.0")
}

tasks {
    shadowJar{
        archiveFileName = "CommandManager-Backend-Common-${version}-all.jar"
        configurations = listOf(
            project.configurations.api.get(),
            project.configurations.implementation.get(),
            project.configurations.modApi.get(),
            project.configurations.modImplementation.get()
        )
    }

    remapJar {
        archiveFileName = "CommandManager-Backend-Common-${version}.jar"
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }
}

configurations{
    implementation{
        isCanBeResolved = true
    }
    api{
        isCanBeResolved=true
    }
}
