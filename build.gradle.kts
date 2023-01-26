plugins {
    kotlin("plugin.serialization") version "1.7.20" apply false
    kotlin("multiplatform") version "1.7.20" apply false
}



allprojects {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    afterEvaluate {
        // Workaround for https://youtrack.jetbrains.com/issue/KT-52776
        rootProject.extensions.findByType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>()?.apply {
            versions.webpackCli.version = "4.10.0"
        }
    }
}
