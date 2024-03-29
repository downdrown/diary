package at.downdrown.diary.frontend.validation

import at.downdrown.diary.api.security.isAuthenticated
import at.downdrown.diary.api.security.password.PasswordStrength
import at.downdrown.diary.api.security.password.PasswordStrengthValidator
import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.extensions.*
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.progressbar.ProgressBar
import com.vaadin.flow.component.progressbar.ProgressBarVariant
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.data.validator.DateRangeValidator
import com.vaadin.flow.data.validator.DateTimeRangeValidator
import com.vaadin.flow.data.validator.EmailValidator
import com.vaadin.flow.data.value.ValueChangeMode
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class Validators(
    private val userService: UserService,
    private val passwordStrengthValidator: PasswordStrengthValidator,
    private val authenticationManager: AuthenticationManager
) {

    companion object {
        fun dateTimeInPast(maxDate: LocalDateTime) : Validator<LocalDateTime> {
            return DateTimeRangeValidator(
                "validator.datetime.past".i18n(),
                LocalDateTime.MIN,
                maxDate)
        }
    }

    fun usernameValidator(): Validator<String> {
        return Validator { givenUsername, context ->

            if (givenUsername == null || givenUsername.isBlank()) {
                ValidationResult.error("validator.username.empty".i18n(givenUsername))
            }

            val field = context.component.get() as TextField
            val usernameAlreadyTaken = userService.exists(givenUsername)
            val isOwnUsername =
                isAuthenticated() && givenUsername != null && givenUsername.contentEquals(userPrincipal().username)

            if (usernameAlreadyTaken && !isOwnUsername) {
                field.colorTextRed()
                ValidationResult.error("validator.username.alreadyexists".i18n(givenUsername))
            } else if (givenUsername.contains(" ")) {
                field.colorTextRed()
                ValidationResult.error("validator.username.illegalformat".i18n(givenUsername))
            } else {
                field.colorTextGreen()
                ValidationResult.ok()
            }
        }
    }

    fun passwordEquals(otherField: PasswordField): Validator<String> {
        return Validator { value, _ ->
            if (otherField.value.isEmpty() || (otherField.value != null && value != null && otherField.value.equals(
                    value
                ))
            ) {
                ValidationResult.ok()
            } else {
                ValidationResult.error("validator.password.nomatch".i18n())
            }
        }
    }

    fun passwordMatchesCurrent(): Validator<String> {
        return Validator { value, _ ->
            if (value.isNullOrBlank()) {
                ValidationResult.ok()
            } else {
                try {
                    authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken(
                            userPrincipal().username,
                            value
                        )
                    )
                    ValidationResult.ok()
                } catch (badCredentials: BadCredentialsException) {
                    ValidationResult.error("validator.password.doesnotmatchcurrent".i18n())
                }
            }
        }
    }

    fun passwordDoesntMatchCurrent(): Validator<String> {
        return Validator { value, _ ->
            if (value.isNullOrBlank()) {
                ValidationResult.ok()
            } else {
                try {
                    authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken(
                            userPrincipal().username,
                            value
                        )
                    )
                    ValidationResult.error("validator.password.matchescurrent".i18n())
                } catch (badCredentials: BadCredentialsException) {
                    ValidationResult.ok()
                }
            }
        }
    }

    fun addDefaultHelperComponentFor(passwordField: PasswordField) {

        val helperText = Span()
        val helperComponent = ProgressBar(0.0, 1.0)
        val helperLayout = HorizontalLayout(helperText, helperComponent)
        helperLayout.hide()
        helperLayout.setWidthFull()
        helperLayout.isSpacing = true
        helperLayout.expand(helperComponent)

        passwordField.helperComponent = helperLayout
        passwordField.valueChangeMode = ValueChangeMode.EAGER

        passwordField.addValueChangeListener { event ->
            if (event.value.isEmpty()) helperLayout.hide() else helperLayout.show()

            helperComponent.removeThemeVariants(
                ProgressBarVariant.LUMO_SUCCESS,
                ProgressBarVariant.LUMO_ERROR
            )
            helperComponent.removeClassName("warning")

            when (passwordStrengthValidator.validatePassword(event.value)) {
                PasswordStrength.Strong -> {
                    helperText.text = "helpercomponent.password.strength.strong".i18n()
                    helperText.colorTextGreen()
                    helperComponent.value = 1.0
                    helperComponent.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS)
                }

                PasswordStrength.Medium -> {
                    helperText.text = "helpercomponent.password.strength.medium".i18n()
                    helperText.colorTextYellow()
                    helperComponent.value = .66
                    helperComponent.addClassName("warning")
                }

                PasswordStrength.Weak -> {
                    helperText.text = "helpercomponent.password.strength.weak".i18n()
                    helperText.colorTextRed()
                    helperComponent.value = .33
                    helperComponent.addThemeVariants(ProgressBarVariant.LUMO_ERROR)
                }
            }
        }
    }

    fun birthdateValidator(): Validator<LocalDate> {
        val today = LocalDate.now()
        return DateRangeValidator(
            "validator.birthdate.invalid".i18n(),
            today.minusYears(100),
            today.minusYears(13)
        )
    }

    fun emailValidator(): Validator<String> {
        return EmailValidator("validator.email.invalid".i18n())
    }
}
