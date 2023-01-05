dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.bootJar {
    enabled = false
}