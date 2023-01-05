package at.downdrown.diary.api.user

import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    var id: Long?,
    var username: String,
    var firstname: String,
    var lastname: String,
    var birthdate: LocalDate?,
    var email: String,
    var registeredAt: LocalDateTime,
    var lastLoginAt: LocalDateTime?
)