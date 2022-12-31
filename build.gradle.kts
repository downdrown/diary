import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.7"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
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

		implementation("org.springframework.boot:spring-boot-starter-actuator")
		//implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-devtools")
		//implementation("org.flywaydb:flyway-core")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
		//runtimeOnly("com.h2database:h2")
		runtimeOnly("io.micrometer:micrometer-registry-influx")
		testImplementation("org.springframework.boot:spring-boot-starter-test")

		implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

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