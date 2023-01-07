package at.downdrown.diary.frontend.extensions

import at.downdrown.diary.api.security.UserPrincipal
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

fun userPrincipal(): UserPrincipal {
    val principal = SecurityContextHolder.getContext().authentication?.principal
    if (principal != null && principal.javaClass.isAssignableFrom(UserPrincipal::class.java)) {
        return principal as UserPrincipal
    }

    throw IllegalStateException("Could not find a valid principal")
}

fun isAuthenticated(): Boolean {
    val authentication = SecurityContextHolder.getContext().authentication
    return authentication != null
            && authentication.isAuthenticated
            && !authentication.javaClass.isAssignableFrom(AnonymousAuthenticationToken::class.java)
}