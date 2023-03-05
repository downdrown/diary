package at.downdrown.diary.frontend.view

import at.downdrown.diary.frontend.view.registration.RegistrationView
import com.vaadin.flow.component.Component

enum class View(
    val navigationTarget: Class<out Component?>,
    val i18nKey: String
) {

    Login(LoginView::class.java, "login.pagetitle"),
    Registration(RegistrationView::class.java, "registration.pagetitle"),
    Today(TodayView::class.java, "nav.today")

}
