plugins {
    `kotlin-dsl`
    `groovy-gradle-plugin`
    kotlin("jvm") version "1.7.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.20"
}

repositories {
    mavenCentral()
    google()
}
val versions: Map<String, String> by rootProject.extra

dependencies {
    implementation("com.android.tools.build:gradle:7.0.4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    implementation("com.charleskorn.kaml:kaml:0.43.0")
}