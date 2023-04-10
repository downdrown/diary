extra["vaadinVersion"] = "24.0.3"

plugins {
    id("com.vaadin") version "24.0.3"
}

repositories {
    maven {
        url = uri("https://maven.vaadin.com/vaadin-addons")
    }
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("org.vaadin.addons.minicalendar:addon:1.4.0")

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
