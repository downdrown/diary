package at.downdrown.diary.frontend.config

import at.downdrown.diary.frontend.view.LoginView
import com.vaadin.flow.spring.security.VaadinWebSecurity
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.*
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter
import java.util.*


@Configuration
@EnableWebSecurity
class FrontendSecurityConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: UserDetailsService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val persistentTokenRepository: PersistentTokenRepository
) : VaadinWebSecurity() {

    // @formatter:off
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.rememberMe()
            .tokenValiditySeconds(60 * 60 * 24 * 7)
            .tokenRepository(persistentTokenRepository)
            .rememberMeServices(rememberMeServices())
        .and()
            .authorizeRequests()
            .antMatchers("/images/**")
            .permitAll()
        .and()
            .authenticationManager(authenticationManager())

        setLoginView(http, LoginView::class.java)

        super.configure(http)
    }
    // @formatter:on

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val providerManager =
            ProviderManager(
                listOf(
                    RememberMeAuthenticationProvider("diaryapp"),
                    authenticationProvider()
                )
            )
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

    @Bean
    fun rememberMeServices(): RememberMeServices {
        val services = PersistentTokenBasedRememberMeServices("diaryapp", userDetailsService, persistentTokenRepository)
        services.setAlwaysRemember(true)
        return services
    }

    @Bean
    fun usernamePasswordAuthenticationFilter(
        rememberMeServices: RememberMeServices,
        authenticationManager: AuthenticationManager
    ): UsernamePasswordAuthenticationFilter {
        val filter = UsernamePasswordAuthenticationFilter()
        filter.rememberMeServices = rememberMeServices
        filter.setAuthenticationManager(authenticationManager)
        return filter
    }

    @Bean
    fun rememberMeAuthenticationFilter(
        authenticationManager: AuthenticationManager,
        rememberMeServices: RememberMeServices
    ): RememberMeAuthenticationFilter {
        return RememberMeAuthenticationFilter(authenticationManager, rememberMeServices)
    }
}