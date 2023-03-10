extra["vaadinVersion"] = "23.3.1"

plugins {
    id("com.vaadin") version "23.3.1"
}

repositories {
    maven {
        url = uri("https://maven.vaadin.com/vaadin-addons")
    }
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.vaadin.addons.minicalendar:addon:1.3.0") {
        // TODO: Remove exclusion once the dependency is removed from the lib
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }

    implementation(project(mapOf("path" to ":api")))
    runtimeOnly(project(mapOf("path" to ":backend")))
}

dependencyManagement {
    imports {
        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

springBoot {
    mainClass.set("at.downdrown.diary.DiaryApplication")
}
