extra["vaadinVersion"] = "23.3.1"

plugins {
	id("com.vaadin") version "23.3.1"
}

dependencies {
	implementation("com.vaadin:vaadin-spring-boot-starter")
}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
	}
}

springBoot {
	mainClass.set( "at.downdrown.diary.DiaryApplication")
}
