import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.polin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation ("com.googlecode.concurrentlinkedhashmap:concurrentlinkedhashmap-lru:1.4.2")
    implementation("org.jetbrains.exposed", "exposed-core", "0.37.3")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.37.3")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.37.3")
    implementation("org.jetbrains.exposed:exposed-java-time:0.38.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
    implementation("org.postgresql:postgresql:42.3.4")
    implementation("com.zaxxer:HikariCP:5.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}