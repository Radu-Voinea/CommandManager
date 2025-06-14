plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

var _version = libs.versions.version.get()
var _group = libs.versions.group.get()

version = _version
group = _group

fun getProperty(name: String): String {
    if (project.hasProperty(name)) {
        return project.findProperty(name) as String
    }

    val envName = name.uppercase().replace(".", "_")

    if (System.getenv().containsKey(envName)) {
        return System.getenv(envName) as String
    }

    return ""
}

fun DependencyHandlerScope.applyDependencies() {
    // Annotations
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    compileOnly(libs.jetbrains.annotations)
    annotationProcessor(libs.jetbrains.annotations)
    testCompileOnly(libs.jetbrains.annotations)
    testAnnotationProcessor(libs.jetbrains.annotations)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
}

fun RepositoryHandler.applyRepositories() {
    mavenCentral()
    maven("https://maven.parchmentmc.org/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.raduvoinea.com/repository/maven-releases/")

    if (getProperty("gg.mmorealms.proxy.europe") == "true") {
        maven(url = getProperty("gg.mmorealms.proxy.europe.url")) {
            name = "Europe-MMORealms-Repository-Proxy"
            credentials(PasswordCredentials::class) {
                username = getProperty("gg.mmorealms.proxy.europe.username")
                password = getProperty("gg.mmorealms.proxy.europe.password")
            }
        }
    } else if(getProperty("gg.mmorealms.url")!="") {
        maven(url = getProperty("gg.mmorealms.url")) {
            name = "MMORealms-Repository"
            credentials(PasswordCredentials::class) {
                username = getProperty("gg.mmorealms.username")
                password = getProperty("gg.mmorealms.password")
            }
        }
    }
}

repositories {
    applyRepositories()
}

dependencies {
    applyDependencies()
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    group = _group
    version = _version

    repositories {
        applyRepositories()
    }

    dependencies {
        applyDependencies()
    }

    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }

        repositories {
            if (project.properties["com.raduvoinea.publish"] == "true") {
                maven(url = (project.findProperty("com.raduvoinea.url") ?: "") as String) {
                    name = "RaduVoinea"
                    credentials(PasswordCredentials::class) {
                        username = (project.findProperty("com.raduvoinea.auth.username") ?: "") as String
                        password = (project.findProperty("com.raduvoinea.auth.password") ?: "") as String
                    }
                }
            }

            if (project.properties["generic.publish"] == "true") {
                maven(url = (project.findProperty("generic.url") ?: "") as String) {
                    name = "Generic"
                    credentials(PasswordCredentials::class) {
                        username = (project.findProperty("generic.auth.username") ?: "") as String
                        password = (project.findProperty("generic.auth.password") ?: "") as String
                    }
                }
            }
        }
    }
}
