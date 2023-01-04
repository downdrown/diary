package at.downdrown.diary.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["at.downdrown.diary.backend.persistence.repository"])
class JpaConfig {
}