import dev.patbeagan.buildsrc.Deps
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.1.0"
//    id("dev.patbeagan.buildsrc.ModuleGraphPluginA") apply true
}
//interface GreetingPluginExtension {
//    val message: Property<String>
//    val greeter: Property<String>
//}
//class GreetingPlugin : Plugin<Project> {
//    override fun apply(project: Project) {
//        val extension = project.extensions.create<GreetingPluginExtension>("greeting")
//        project.task("hello") {
//            doLast {
//                println("${extension.message.get()} from ${extension.greeter.get()}")
//            }
//        }
//    }
//}
//
//apply<GreetingPlugin>()
apply<dev.patbeagan.buildsrc.ModuleGraphPluginA>()

group = "dev.patbeagan"
version = "1.0"
val githubCredentials: MavenArtifactRepository.() -> Unit = {
    credentials {
        username = project.findProperty("gpr.username") as String?
            ?: System.getenv("GITHUB_USERNAME")
        password = project.findProperty("gpr.personal_access_token") as String?
            ?: System.getenv("GITHUB_PERSONAL_ACCESS_TOKEN")
    }
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
//    maven("https://maven.pkg.github.com/patbeagan1/ProtocolRSS", githubCredentials)
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
//    implementation(Deps.protocolRss)
    implementation(Deps.h2)
    implementation(Deps.sqlite)
    implementation(Deps.rome)
    implementation(project(":data"))
    implementation(project(":domain"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "AggronRssReader"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType(KotlinCompile::class.java).configureEach {
    kotlinOptions {
        if (project.findProperty("aggron.enableComposeCompilerReports") == "true") {
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
            )
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_metrics"
            )
        }
    }
}
