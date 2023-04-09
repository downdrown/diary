package at.downdrown.diary.frontend.service

import at.downdrown.diary.frontend.view.View
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Service

@Service
class AuthenticationService {

    fun logout() {
        UI.getCurrent().navigate(View.Login.navigationTarget)
        val httpServletRequest = VaadinServletRequest.getCurrent().httpServletRequest
        httpServletRequest.logout()
        SecurityContextLogoutHandler().logout(httpServletRequest, null, null)
    }

    fun isAuthenticated(): Boolean {
        return SecurityContextHolder.getContext().authentication.isAuthenticated
    }
}
