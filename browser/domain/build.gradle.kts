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
    implementation("rome:rome:1.0")
}
