extra["vaadinVersion"] = "23.3.1"

plugins {
	id("com.vaadin") version "23.3.1"
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.vaadin:vaadin-spring-boot-starter")

	implementation(project(mapOf("path" to ":api")))
	runtimeOnly(project(mapOf("path" to ":backend")))
}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
	}
}

springBoot {
	mainClass.set( "at.downdrown.diary.DiaryApplication")
}
