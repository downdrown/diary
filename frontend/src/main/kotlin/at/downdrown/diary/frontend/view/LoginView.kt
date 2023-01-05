package at.downdrown.diary.frontend.view

import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.extensions.navigate
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.flow.theme.lumo.LumoUtility

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
class LoginView : VerticalLayout(), BeforeEnterObserver {

    private var hasLoginError = false

    init {
        setupLayout()
        add(loginOverlay())
    }

    private fun setupLayout() {
        setSizeFull()
        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        isSpacing = false
        isPadding = false
    }

    private fun loginOverlay(): LoginOverlay {
        val loginOverlay = LoginOverlay(setupLoginI18n())
        loginOverlay.isOpened = true
        loginOverlay.action = "login"
        loginOverlay.addClassName(LumoUtility.Padding.NONE)
        loginOverlay.isError = hasLoginError

        loginOverlay.addForgotPasswordListener {
            loginOverlay.isOpened = false
            navigate(View.Registration)
        }

        return loginOverlay
    }

    private fun setupLoginI18n(): LoginI18n {

        val loginI18nHeader = LoginI18n.Header()
        loginI18nHeader.title = i18n("login.header.title")
        loginI18nHeader.description = i18n("login.header.description")

        val loginI18nForm = LoginI18n.Form()
        loginI18nForm.title = i18n("login.form.title")
        loginI18nForm.username = i18n("login.form.username")
        loginI18nForm.password = i18n("login.form.password")
        loginI18nForm.submit = i18n("login.form.submit")
        loginI18nForm.forgotPassword = i18n("login.form.forgotPassword")

        val loginI18nErrorMessage = LoginI18n.ErrorMessage()
        loginI18nErrorMessage.title = i18n("login.error.title")
        loginI18nErrorMessage.message = i18n("login.error.message")

        val loginI18n = LoginI18n()
        loginI18n.header = loginI18nHeader
        loginI18n.form = loginI18nForm
        loginI18n.errorMessage = loginI18nErrorMessage

        return loginI18n
    }

    override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.location
                .queryParameters
                .parameters
                .containsKey("error")
        ) {
            hasLoginError = true
        }
    }
}