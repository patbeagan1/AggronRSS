plugins {
    kotlin("jvm")
}

group = "dev.patbeagan"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}
val exposedVersion: String by project
val ktor_version: String by project
dependencies {
    implementation(project(":domain"))
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    implementation("rome:rome:1.0")

    implementation("com.h2database:h2:2.1.212")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
}
