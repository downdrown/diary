import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("plugin.jpa") version "1.8.20"
}

group = "at.downdrown.diary"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_18

repositories {
    mavenCentral()
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

    repositories {
        mavenCentral()
    }

    dependencies {

        // Kotlin
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        // Spring
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-devtools")
        runtimeOnly("io.micrometer:micrometer-registry-influx")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        // Util
        implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "18"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.bootJar {
    enabled = false
}
tasks.jar {
    enabled = false
}
