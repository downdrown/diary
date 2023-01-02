dependencies {

    implementation(project(mapOf("path" to ":api")))

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")

    runtimeOnly("com.h2database:h2")
}

tasks.bootJar {
    enabled = false
}