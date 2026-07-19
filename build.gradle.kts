plugins {
    id("java-library")
    id("com.gradleup.shadow") version "8.3.8"
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("org.bstats:bstats-bukkit:3.2.1")
    implementation("org.jsoup:jsoup:1.18.1")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
        archiveFileName.set("${archiveBaseName.get()}.jar")

        destinationDirectory.set(file("D:/servers/1.21.11/plugins"))

        relocate("org.jsoup", "${project.group}.shaded.jsoup")

        from(sourceSets.main.get().output)
    }

    build {
        dependsOn(shadowJar)
    }
}