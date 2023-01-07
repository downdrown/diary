package at.downdrown.diary.frontend.validation

import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.extensions.*
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.ValidationResult
import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.data.validator.DateRangeValidator
import com.vaadin.flow.data.validator.EmailValidator
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class Validators(
    private val userService: UserService
) {
    fun usernameValidator(): Validator<String> {
        return Validator { givenUsername, context ->

           if (givenUsername == null || givenUsername.isBlank()) {
                ValidationResult.error(i18n("validator.username.empty", givenUsername))
            }

            val field = context.component.get() as TextField
            val usernameAlreadyTaken = userService.exists(givenUsername)
            val isOwnUsername = isAuthenticated() && givenUsername != null && givenUsername.contentEquals(userPrincipal().username)

            if (usernameAlreadyTaken && !isOwnUsername) {
                field.colorTextRed()
                ValidationResult.error(i18n("validator.username.alreadyexists", givenUsername))
            } else if (givenUsername.contains(" ")) {
                field.colorTextRed()
                ValidationResult.error(i18n("validator.username.illegalformat", givenUsername))
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
                ValidationResult.error(i18n("validator.password.nomatch"))
            }
        }
    }

    fun birthdateValidator(): Validator<LocalDate> {
        val today = LocalDate.now()
        return DateRangeValidator(
            i18n("validator.birthdate.invalid"),
            today.minusYears(100),
            today.minusYears(13)
        )
    }

    fun emailValidator(): Validator<String> {
        return EmailValidator(i18n("validator.email.invalid"))
    }
}