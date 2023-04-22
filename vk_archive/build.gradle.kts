
import org.gradle.jvm.tasks.Jar
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.5.0"
    java
}

group = "ru.mse"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation ("org.jsoup:jsoup:1.15.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                implementation("com.mikepenz:multiplatform-markdown-renderer:0.6.1")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "AppKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "vk_archive"
            packageVersion = "1.0.0"
        }
    }
}

/**
 * build: ./gradlew buildAllJars - build jars for all platforms
 *        ./gradlew buildJarWindows - build jar for Windows
 *        ./gradlew buildJarLinux - build jar for Linux
 *        ./gradlew buildJarMacOs - build jar for Mac-os
 * run jar: (из папки /build/libs) java -jar vk_archive-1.0.0-{your OS}}.jar
 */
tasks {
    register<Jar>("buildJarWindows") {
        group = "jarBuild"
        archiveClassifier.set("windows")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "app.AppKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(sourceSets.main.get().output)
        enabled = true
    }

    register<Jar>("buildJarLinux") {
        group = "jarBuild"
        archiveClassifier.set("linux")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "app.AppKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(sourceSets.main.get().output)
        enabled = true
    }

    register<Jar>("buildJarMacOs") {
        group = "jarBuild"
        archiveClassifier.set("macos")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "app.AppKt"
        }
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        from(sourceSets.main.get().output)
        enabled = true
    }

    register("buildAllJars") {
        dependsOn("buildJarWindows", "buildJarLinux", "buildJarMacOs")
    }

    withType<Test> {
        useJUnitPlatform()
    }
}