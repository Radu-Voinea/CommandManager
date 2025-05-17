rootProject.name = "command-manager"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev")
        maven("https://maven.parchmentmc.org")
        maven("https://files.minecraftforge.net/maven")
        mavenCentral()
    }
}

fun defineProject(module: String, path: String) {
    include(module)
    project(module).projectDir = file(path)
}

defineProject(":common", "src/common")
defineProject(":backend.common", "src/backend/common")
defineProject(":backend.neoforge", "src/backend/neoforge")
defineProject(":backend.fabric", "src/backend/fabric")
defineProject(":velocity", "src/velocity")
// TODO: Spigot (Bukkit) Support
