package at.downdrown.diary.api.user

import java.time.LocalDate
import java.time.LocalDateTime

data class User(
    val id: Long?,
    val username: String,
    val firstname: String,
    val lastname: String,
    val birthdate: LocalDate?,
    val email: String,
    val registeredAt: LocalDateTime,
    val lastLoginAt: LocalDateTime?
)