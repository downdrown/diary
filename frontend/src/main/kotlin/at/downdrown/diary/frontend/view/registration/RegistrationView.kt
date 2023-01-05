package at.downdrown.diary.frontend.view.registration

import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.extensions.*
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
import com.vaadin.flow.component.notification.Notification
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
import com.vaadin.flow.data.validator.DateRangeValidator
import com.vaadin.flow.data.validator.EmailValidator
import com.vaadin.flow.router.HasDynamicTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import java.time.LocalDate
import kotlin.Exception
import kotlin.String

@Route("/register")
@AnonymousAllowed
class RegistrationView(
    private val userService: UserService
) : VerticalLayout(), HasDynamicTitle {

    private val isMobile = UI.getCurrent().isMobile()

    init {

        alignItems = FlexComponent.Alignment.CENTER
        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        isSpacing = true

        val tabSheet = TabSheet()
        tabSheet.setWidth(if (isMobile) 100f else 50f, Unit.PERCENTAGE)
        tabSheet.setMaxWidth(700f, Unit.PIXELS)
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_BORDERED)
        tabSheet.add(Tab(i18n("registration.tab.register")), registerSubView())
        tabSheet.add(Tab(i18n("registration.tab.reset")), VerticalLayout()).isEnabled = false

        add(tabSheet)
    }

    private fun registerSubView(): Component {

        val username = TextField(i18n("registration.form.username"))
        username.prefixComponent = VaadinIcon.AT.create()
        username.helperText = i18n("registration.form.username.helpertext")
        username.placeholder = i18n("registration.form.username.placeholder")

        val firstname = TextField(i18n("registration.form.firstname"))
        firstname.autocapitalize = Autocapitalize.WORDS

        val lastname = TextField(i18n("registration.form.lastname"))
        lastname.autocapitalize = Autocapitalize.WORDS

        val birthdate = DatePicker(i18n("registration.form.birthdate"))
        val email = EmailField(i18n("registration.form.email"))
        val password = PasswordField(i18n("registration.form.password"))
        val confirmPassword = PasswordField(i18n("registration.form.confirmPassword"))

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
            .withValidator(usernameIsValid())
            .withValidator(usernameDoesNotExist())
            .bind(UserRegistrationFormModel::username.getter, UserRegistrationFormModel::username.setter)

        binder.forField(firstname)
            .asRequired()
            .bind(UserRegistrationFormModel::firstname.getter, UserRegistrationFormModel::firstname.setter)

        binder.forField(lastname)
            .asRequired()
            .bind(UserRegistrationFormModel::lastname.getter, UserRegistrationFormModel::lastname.setter)

        binder.forField(birthdate)
            .withValidator(
                DateRangeValidator(
                    i18n("registration.form.validation.birthdate"),
                    LocalDate.now().minusYears(100),
                    LocalDate.now()
                )
            )
            .bind(UserRegistrationFormModel::birthdate.getter, UserRegistrationFormModel::birthdate.setter)

        binder.forField(email)
            .asRequired()
            .withValidator(EmailValidator(i18n("registration.form.validation.email")))
            .bind(UserRegistrationFormModel::email.getter, UserRegistrationFormModel::email.setter)

        binder.forField(password)
            .asRequired()
            .withValidator(passwordEquals(confirmPassword))
            .bind(UserRegistrationFormModel::password.getter, UserRegistrationFormModel::password.setter)

        binder.forField(confirmPassword)
            .asRequired()
            .withValidator(passwordEquals(password))
            .bind(UserRegistrationFormModel::confirmPassword.getter, UserRegistrationFormModel::confirmPassword.setter)

        binder.bean = UserRegistrationFormModel()

        val submitButton = Button(i18n("registration.form.submit")) { event ->
            try {
                val userRegistrationFormModel = binder.bean
                userService.register(userRegistrationFormModel.toUser(), userRegistrationFormModel.password)
                Notification.show(i18n("registration.success", userRegistrationFormModel.username), 3000, Notification.Position.BOTTOM_END)
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

        val backToLoginButton = Button(i18n("registration.action.login")) { navigate(View.Login) }
        backToLoginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
        backToLoginButton.addThemeVariants(ButtonVariant.LUMO_SMALL)

        val actionsLayout = HorizontalLayout(submitButton, backToLoginButton)
        actionsLayout.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
        actionsLayout.isSpacing = true

        return VerticalLayout(
            H3(i18n("registration.header.title")),
            Text(i18n("registration.header.description")),
            formLayout,
            actionsLayout
        )
    }

    private fun usernameIsValid(): Validator<String> {
        return Validator { value, context ->
            val field = context.component.get() as TextField
            if (value.contains(" ")) {
                field.colorTextRed()
                ValidationResult.error(i18n("registration.form.validation.username.invalid", value))
            } else {
                field.colorTextGreen()
                ValidationResult.ok()
            }
        }
    }

    private fun usernameDoesNotExist(): Validator<String> {
        return Validator { value, context ->
            val field = context.component.get() as TextField
            if (userService.exists(value)) {
                field.colorTextRed()
                ValidationResult.error(i18n("registration.form.validation.username.exists", value))
            } else {
                field.colorTextGreen()
                ValidationResult.ok()
            }
        }
    }

    private fun passwordEquals(otherField: PasswordField): Validator<String> {
        return Validator { value, _ ->
            if (otherField.value.isEmpty() || (otherField.value != null && value != null && otherField.value.equals(
                    value
                ))
            ) {
                ValidationResult.ok()
            } else {
                ValidationResult.error(i18n("registration.form.validation.password.nomatch"))
            }
        }
    }

    override fun getPageTitle(): String {
        return i18n("registration.pagetitle")
    }
}