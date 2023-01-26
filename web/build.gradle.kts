import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

val serializationVersion = "1.3.3"
val ktorVersion = "2.0.3"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.2.2"
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                devServer?.`open` = false // <------ HERE
                devServer?.port = 8081
                outputFileName = "web.js"
            }

            useCommonJs()
        }
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":common"))

                implementation("io.ktor:ktor-client-js:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation(npm("@material-ui/icons", "4.11.2"))
            }
        }
    }
}

// a temporary workaround for a bug in jsRun invocation - see https://youtrack.jetbrains.com/issue/KT-48273
afterEvaluate {
    rootProject.extensions.configure<NodeJsRootExtension> {
        versions.webpackDevServer.version = "4.0.0"
        versions.webpackCli.version = "4.10.0"
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

