package at.downdrown.diary.frontend.config

import at.downdrown.diary.frontend.view.LoginView
import com.vaadin.flow.spring.security.VaadinWebSecurity
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.*
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@Configuration
@EnableWebSecurity
class FrontendSecurityConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsService,
    private val applicationEventPublisher: ApplicationEventPublisher
) : VaadinWebSecurity() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.authorizeRequests().antMatchers("/images/**").permitAll()
        http.authenticationManager(authenticationManager())

        super.configure(http)
        setLoginView(http, LoginView::class.java)
    }

    private fun authenticationManager(): AuthenticationManager {
        val providerManager = ProviderManager(listOf(authenticationProvider()))
        providerManager.setAuthenticationEventPublisher(authenticationEventPublisher(applicationEventPublisher))
        return providerManager
    }

    private fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        authenticationProvider.setUserDetailsService(userDetailsService)
        return authenticationProvider
    }

    private fun authenticationEventPublisher(applicationEventPublisher: ApplicationEventPublisher?): AuthenticationEventPublisher {
        return DefaultAuthenticationEventPublisher(applicationEventPublisher)
    }
}