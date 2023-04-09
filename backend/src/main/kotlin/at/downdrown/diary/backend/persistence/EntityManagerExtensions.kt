package at.downdrown.diary.backend.persistence

import at.downdrown.diary.api.security.userPrincipal
import at.downdrown.diary.backend.persistence.entity.UserEntity
import jakarta.persistence.EntityManager

fun EntityManager.currentUserReference(): UserEntity {
    return getReference(UserEntity::class.java, userPrincipal().user.id)
}
