package at.downdrown.diary.frontend.validation

import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.frontend.extensions.colorTextGreen
import at.downdrown.diary.frontend.extensions.colorTextRed
import at.downdrown.diary.frontend.extensions.i18n
import at.downdrown.diary.frontend.extensions.userPrincipal
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
        return Validator { value, context ->
            val loggedInUser = userPrincipal().user
            val field = context.component.get() as TextField
            if (!value.equals(loggedInUser.username) && userService.exists(value)) {
                field.colorTextRed()
                ValidationResult.error(i18n("validator.username.alreadyexists", value))
            } else if (value.contains(" ")) {
                field.colorTextRed()
                ValidationResult.error(i18n("validator.username.illegalformat", value))
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