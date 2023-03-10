package at.downdrown.diary.backend.user

import at.downdrown.diary.api.user.User
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.backend.config.CACHE_USERS_BY_USERNAME
import at.downdrown.diary.backend.config.CACHE_USERS_EXIST_BY_USERNAME
import at.downdrown.diary.backend.persistence.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    @CacheEvict(cacheNames = [CACHE_USERS_BY_USERNAME, CACHE_USERS_EXIST_BY_USERNAME], allEntries = true)
    override fun register(user: User, password: String) {
        val encodedPassword = passwordEncoder.encode(password)
        user.registeredAt = LocalDateTime.now()
        userRepository.save(user.toEntity(encodedPassword))
    }

    @CacheEvict(cacheNames = [CACHE_USERS_BY_USERNAME, CACHE_USERS_EXIST_BY_USERNAME], key = "#user.username")
    override fun update(user: User) {
        user.id?.let { userId ->
            val dbUser = userRepository.findById(userId).orElseThrow()
            dbUser.username = user.username
            dbUser.firstname = user.firstname
            dbUser.lastname = user.lastname
            dbUser.birthdate = user.birthdate
            dbUser.email = user.email
            dbUser.lastLoginAt = user.lastLoginAt

            userRepository.save(dbUser)
        }
    }

    @CacheEvict(cacheNames = [CACHE_USERS_BY_USERNAME, CACHE_USERS_EXIST_BY_USERNAME], allEntries = true)
    override fun delete(userId: Long) {
        userRepository.deleteById(userId)
    }

    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)?.toDomain()
    }

    override fun exists(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    override fun changePassword(username: String, currentPassword: String, newPassword: String) {
        val dbPassword = userRepository.findByUsername(username)?.password
        dbPassword.let {
            if (passwordEncoder.matches(currentPassword, it)) {
                val encodedPassword = passwordEncoder.encode(newPassword)
                userRepository.updateUserPassword(encodedPassword, username)
            }
        }
    }

    override fun updateLastLogin(username: String) {
        userRepository.updateLastLoginTimestamp(username)
    }
}