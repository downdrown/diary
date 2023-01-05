package at.downdrown.diary.frontend.event

import at.downdrown.diary.api.user.UserService
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class AuthenticationEventHandler(private val userService: UserService) {

    @EventListener
    fun onSuccessfulAuthentication(event: AuthenticationSuccessEvent) {
        userService.findByUsername((event.authentication.principal as User).username)?.let { loggedInUser ->
            userService.updateLastLogin(loggedInUser.username)
            log.debug { "Updated last login timestamp for ${loggedInUser.username}" }
        }
    }
}