plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "1.5.31"
}

group = "ru.mastkey.fj_2024"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("ch.qos.logback:logback-classic:1.3.11")

    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("org.mockito:mockito-inline:5.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}