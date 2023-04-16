package at.downdrown.diary.frontend.view.registration

import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.Notifications
import at.downdrown.diary.frontend.extensions.*
import at.downdrown.diary.frontend.service.AuthenticationService
import at.downdrown.diary.frontend.validation.Validators
import at.downdrown.diary.frontend.view.View
import com.vaadin.flow.component.*
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep
import com.vaadin.flow.component.html.H3
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.TabSheet
import com.vaadin.flow.component.tabs.TabSheetVariant
import com.vaadin.flow.component.textfield.Autocapitalize
import com.vaadin.flow.component.textfield.EmailField
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.*
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import kotlin.Exception
import kotlin.String

@Route("/register")
@AnonymousAllowed
class RegistrationView(
    private val authenticationService: AuthenticationService,
    private val userService: UserService,
    private val validators: Validators
) : VerticalLayout(), HasDynamicTitle, BeforeEnterObserver {

    init {

        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        isSpacing = true

        val tabSheet = TabSheet()
        tabSheet.setWidth(if (isMobile()) 100f else 50f, Unit.PERCENTAGE)
        tabSheet.setMaxWidth(700f, Unit.PIXELS)
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_BORDERED)
        tabSheet.add(Tab("registration.tab.register".i18n()), registerSubView())
        tabSheet.add(Tab("registration.tab.reset".i18n()), VerticalLayout()).isEnabled = false

        add(tabSheet)
    }

    private fun registerSubView(): Component {

        val username = TextField("registration.form.username".i18n())
        username.prefixComponent = VaadinIcon.AT.create()
        username.helperText = "registration.form.username.helpertext".i18n()
        username.placeholder = "registration.form.username.placeholder".i18n()

        val firstname = TextField("registration.form.firstname".i18n())
        firstname.autocapitalize = Autocapitalize.WORDS

        val lastname = TextField("registration.form.lastname".i18n())
        lastname.autocapitalize = Autocapitalize.WORDS

        val birthdate = DatePicker("registration.form.birthdate".i18n())
        val email = EmailField("registration.form.email".i18n())
        val password = PasswordField("registration.form.password".i18n())
        validators.addDefaultHelperComponentFor(password)

        val confirmPassword = PasswordField("registration.form.confirmPassword".i18n())

        val formLayout = FormLayout(
            username,
            firstname,
            lastname,
            birthdate,
            email,
            password,
            confirmPassword
        )

        formLayout.setResponsiveSteps(
            ResponsiveStep("0", 1),
            ResponsiveStep("500px", 2)
        )

        formLayout.setColspan(username, 2)
        formLayout.setWidthFull()

        val binder = Binder(UserRegistrationFormModel::class.java)

        binder.forField(username)
            .asRequired()
            .withValidator(validators.usernameValidator())
            .bind(UserRegistrationFormModel::username.getter, UserRegistrationFormModel::username.setter)

        binder.forField(firstname)
            .asRequired()
            .bind(UserRegistrationFormModel::firstname.getter, UserRegistrationFormModel::firstname.setter)

        binder.forField(lastname)
            .asRequired()
            .bind(UserRegistrationFormModel::lastname.getter, UserRegistrationFormModel::lastname.setter)

        binder.forField(birthdate)
            .withValidator(validators.birthdateValidator())
            .bind(UserRegistrationFormModel::birthdate.getter, UserRegistrationFormModel::birthdate.setter)

        binder.forField(email)
            .asRequired()
            .withValidator(validators.emailValidator())
            .bind(UserRegistrationFormModel::email.getter, UserRegistrationFormModel::email.setter)

        binder.forField(password)
            .asRequired()
            .withValidator(validators.passwordEquals(confirmPassword))
            .bind(UserRegistrationFormModel::password.getter, UserRegistrationFormModel::password.setter)

        binder.forField(confirmPassword)
            .asRequired()
            .withValidator(validators.passwordEquals(password))
            .bind(UserRegistrationFormModel::confirmPassword.getter, UserRegistrationFormModel::confirmPassword.setter)

        binder.bean = UserRegistrationFormModel()

        val submitButton = Button("registration.form.submit".i18n()) { event ->
            try {
                val userRegistrationFormModel = binder.bean
                userService.register(userRegistrationFormModel.toUser(), userRegistrationFormModel.password)
                Notifications.showRegistrationSuccess(userRegistrationFormModel.username)
                navigate(View.Login)
            } catch (e: Exception) {
                if (!event.source.isEnabled) {
                    event.source.isEnabled = true
                }
            }
        }
        submitButton.isEnabled = false
        submitButton.isDisableOnClick = true
        submitButton.addClickShortcut(Key.ENTER)
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY)

        binder.addValueChangeListener {
            submitButton.isEnabled = binder.isValid
        }

        val backToLoginButton = Button("registration.action.login".i18n()) { navigate(View.Login) }
        backToLoginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        backToLoginButton.addThemeVariants(ButtonVariant.LUMO_SMALL)

        val actionsLayout = HorizontalLayout(submitButton, backToLoginButton)
        actionsLayout.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        actionsLayout.isSpacing = true

        return VerticalLayout(
            H3("registration.header.title".i18n()),
            Text("registration.header.description".i18n()),
            formLayout,
            actionsLayout
        )
    }

    override fun getPageTitle(): String {
        return "registration.pagetitle".i18n()
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        if (authenticationService.isAuthenticated()) {
            event.forwardTo(View.Today.navigationTarget)
        }
    }
}
