package at.downdrown.diary.backend.user

import at.downdrown.diary.api.user.User
import at.downdrown.diary.backend.persistence.entity.UserEntity

fun User.toEntity(password: String): UserEntity {
    return UserEntity(
        id = id,
        username = username,
        firstname = firstname,
        lastname = lastname,
        birthdate = birthdate,
        email = email,
        registeredAt = registeredAt,
        lastLoginAt = lastLoginAt,
        password = password,
        moodCheckIns = emptyList()
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        username = username,
        firstname = firstname,
        lastname = lastname,
        birthdate = birthdate,
        email = email,
        registeredAt = registeredAt,
        lastLoginAt = lastLoginAt
    )
}
