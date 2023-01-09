package at.downdrown.diary.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import javax.sql.DataSource

@Configuration
class BackendSecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun persistentTokenRepository(dataSource: DataSource): PersistentTokenRepository {
        val persistentTokenRepository = JdbcTokenRepositoryImpl()
        persistentTokenRepository.setDataSource(dataSource)
        return persistentTokenRepository
    }
}
