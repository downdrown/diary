package at.downdrown.diary.frontend.view.registration

import at.downdrown.diary.api.user.User
import java.time.LocalDate
import java.time.LocalDateTime

data class UserRegistrationFormModel(
    var id: Long?,
    var username: String,
    var firstname: String,
    var lastname: String,
    var birthdate: LocalDate?,
    var email: String,
    var password: String,
    var confirmPassword: String
) {
    constructor() : this(
        null,
        "",
        "",
        "",
        null,
        "",
        "",
        ""
    )

    fun toUser(): User {
        return User(
            null,
            username,
            firstname,
            lastname,
            birthdate,
            email,
            LocalDateTime.now(),
            null
        )
    }
}
