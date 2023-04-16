package at.downdrown.diary.frontend.view

import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.extensions.navigate
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.*
import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.flow.theme.lumo.LumoUtility

@Route("login")
@AnonymousAllowed
class LoginView : VerticalLayout(), HasDynamicTitle, BeforeEnterObserver {

    private lateinit var onHasLoginErrorChange: (Boolean) -> Unit

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

        onHasLoginErrorChange = loginOverlay::setError

        loginOverlay.addForgotPasswordListener {
            loginOverlay.isOpened = false
            navigate(View.Registration)
        }

        return loginOverlay
    }

    private fun setupLoginI18n(): LoginI18n {

        val loginI18nHeader = LoginI18n.Header()
        loginI18nHeader.title = "login.header.title".i18n()
        loginI18nHeader.description = "login.header.description".i18n()

        val loginI18nForm = LoginI18n.Form()
        loginI18nForm.title = "login.form.title".i18n()
        loginI18nForm.username = "login.form.username".i18n()
        loginI18nForm.password = "login.form.password".i18n()
        loginI18nForm.submit = "login.form.submit".i18n()
        loginI18nForm.forgotPassword = "login.form.forgotPassword".i18n()

        val loginI18nErrorMessage = LoginI18n.ErrorMessage()
        loginI18nErrorMessage.title = "login.error.title".i18n()
        loginI18nErrorMessage.message = "login.error.message".i18n()

        val loginI18n = LoginI18n()
        loginI18n.header = loginI18nHeader
        loginI18n.form = loginI18nForm
        loginI18n.errorMessage = loginI18nErrorMessage

        return loginI18n
    }

    override fun beforeEnter(beforeEnterEvent: BeforeEnterEvent) {
        checkForLoginError(beforeEnterEvent.location)
    }

    private fun checkForLoginError(location: Location) {
        if (location
                .queryParameters
                .parameters
                .containsKey("error")
        ) {
            onHasLoginErrorChange.invoke(true)
        }
    }

    override fun getPageTitle(): String {
        return "login.pagetitle".i18n()
    }
}
