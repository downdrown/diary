package at.downdrown.diary.api.security.password

import org.springframework.stereotype.Service

@Service
class DefaultPasswordStrengthValidator : PasswordStrengthValidator {

    private val strongPasswordMatcher =
        Regex("^.*(?=.{15,})(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).*\$")
    private val mediumPasswordMatcher = Regex("^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\\d).*\$")

    override fun validatePassword(plainTextPassword: String): PasswordStrength {
        return if (strongPasswordMatcher.matches(plainTextPassword)) {
            PasswordStrength.Strong
        } else if (mediumPasswordMatcher.matches(plainTextPassword)) {
            PasswordStrength.Medium
        } else {
            PasswordStrength.Weak
        }
    }
}
