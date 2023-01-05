package at.downdrown.diary.frontend.extensions

import at.downdrown.diary.api.security.UserPrincipal
import org.springframework.security.core.context.SecurityContextHolder

fun userPrincipal(): UserPrincipal {
    return SecurityContextHolder.getContext().authentication?.principal as UserPrincipal
}