package at.downdrown.diary.api.security.password

interface PasswordStrengthValidator {

    fun validatePassword(plainTextPassword: String): PasswordStrength

}