package at.downdrown.diary.backend.user

import at.downdrown.diary.api.user.User
import at.downdrown.diary.api.user.UserService
import at.downdrown.diary.backend.persistence.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override fun register(user: User, password: String) {
        val encodedPassword = passwordEncoder.encode(password)
        userRepository.save(user.toEntity(encodedPassword))
    }

    override fun update(user: User) {
        user.id?.let {
            val dbUser = userRepository.findById(it).orElseThrow()
            dbUser.username = user.username
            dbUser.firstname = user.firstname
            dbUser.lastname = user.lastname
            dbUser.birthdate = user.birthdate
            dbUser.email = user.email

            userRepository.save(dbUser)
        }
    }

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
}