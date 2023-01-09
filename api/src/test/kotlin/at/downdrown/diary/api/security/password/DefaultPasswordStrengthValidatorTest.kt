package at.downdrown.diary.api.security.password

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DefaultPasswordStrengthValidatorTest {

    private val defaultPasswordStrengthValidator = DefaultPasswordStrengthValidator()

    @Test
    fun validateStrongPasswords() {
        assertEquals(
            PasswordStrength.Strong,
            defaultPasswordStrengthValidator.validatePassword("Lp{B0T:ua:>F~oH")
        )
        assertEquals(
            PasswordStrength.Strong,
            defaultPasswordStrengthValidator.validatePassword("r[^~Wnyo,>Wv8og")
        )
        assertEquals(
            PasswordStrength.Strong,
            defaultPasswordStrengthValidator.validatePassword("S<I:bb3s;vK]TJ|{_9xuBRmKC>)Z-9")
        )
        assertEquals(
            PasswordStrength.Strong,
            defaultPasswordStrengthValidator.validatePassword("50]wubSM}JtN\\8}\\L^>8Gw@;|O'q79")
        )
    }

    @Test
    fun validateMediumPasswords() {
        assertEquals(
            PasswordStrength.Medium,
            defaultPasswordStrengthValidator.validatePassword("uUnc5X0y")
        )
        assertEquals(
            PasswordStrength.Medium,
            defaultPasswordStrengthValidator.validatePassword("0z3LIGe8")
        )
        assertEquals(
            PasswordStrength.Medium,
            defaultPasswordStrengthValidator.validatePassword("lYaFX7BbzSj12NUn3hYA")
        )
        assertEquals(
            PasswordStrength.Medium,
            defaultPasswordStrengthValidator.validatePassword("SmzwDg4EGeQoGlGNoe81")
        )
    }

    @Test
    fun validateWeakPasswords() {
        assertEquals(
            PasswordStrength.Weak,
            defaultPasswordStrengthValidator.validatePassword("start")
        )
        assertEquals(
            PasswordStrength.Weak,
            defaultPasswordStrengthValidator.validatePassword("password")
        )
        assertEquals(
            PasswordStrength.Weak,
            defaultPasswordStrengthValidator.validatePassword("ilovemydoggohoneyboo")
        )
        assertEquals(
            PasswordStrength.Weak,
            defaultPasswordStrengthValidator.validatePassword("thisisaridiculouslongpasswordthatisstillcrap")
        )
    }
}
