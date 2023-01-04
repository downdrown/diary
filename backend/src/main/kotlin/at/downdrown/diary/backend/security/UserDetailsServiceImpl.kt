package at.downdrown.diary.backend.security

import at.downdrown.diary.backend.persistence.entity.UserEntity
import at.downdrown.diary.backend.persistence.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username != null) {
            return userRepository.findByUsername(username)?.toUserDetails()
        }
        throw UsernameNotFoundException("User $username not found")
    }

    private fun UserEntity.toUserDetails(): UserDetails {
        return org.springframework.security.core.userdetails.User(
            username,
            password,
            true,
            true,
            true,
            true,
            mutableListOf()
        )
    }
}
