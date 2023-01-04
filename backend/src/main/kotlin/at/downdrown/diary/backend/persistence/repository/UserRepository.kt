package at.downdrown.diary.backend.persistence.repository

import at.downdrown.diary.backend.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository


@Repository
interface UserRepository : CrudRepository<UserEntity, Long> {

    fun findByUsername(username: String): UserEntity?

    fun existsByUsername(username: String): Boolean

    @Modifying
    @Query("update UserEntity u set u.password = :password where u.username = :username")
    fun updateUserPassword(
        @Param("password") password: String,
        @Param("username") username: String
    ): Int

}