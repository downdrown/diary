package at.downdrown.diary.backend.persistence.repository

import at.downdrown.diary.backend.config.CACHE_USERS_BY_USERNAME
import at.downdrown.diary.backend.config.CACHE_USERS_EXIST_BY_USERNAME
import at.downdrown.diary.backend.persistence.entity.UserEntity
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : CrudRepository<UserEntity, Long> {

    @Cacheable(CACHE_USERS_BY_USERNAME, key = "#username")
    fun findByUsername(username: String): UserEntity?

    @Cacheable(CACHE_USERS_EXIST_BY_USERNAME, key = "#username")
    fun existsByUsername(username: String): Boolean

    @CacheEvict(cacheNames = [CACHE_USERS_BY_USERNAME, CACHE_USERS_EXIST_BY_USERNAME], key = "#username")
    @Modifying
    @Query("update UserEntity u set u.password = :password where u.username = :username")
    @Transactional
    fun updateUserPassword(
        @Param("password") password: String,
        @Param("username") username: String
    )

    @Modifying
    @Query("update UserEntity u set u.lastLoginAt = current_timestamp() where u.username = :username")
    @Transactional
    fun updateLastLoginTimestamp(
        @Param("username") username: String
    )
}