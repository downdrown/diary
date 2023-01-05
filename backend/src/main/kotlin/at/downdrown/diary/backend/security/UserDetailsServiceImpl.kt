package at.downdrown.diary.backend.security

import at.downdrown.diary.api.security.UserPrincipal
import at.downdrown.diary.backend.persistence.entity.UserEntity
import at.downdrown.diary.backend.persistence.repository.UserRepository
import at.downdrown.diary.backend.user.toDomain
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username != null) {
            return userRepository.findByUsername(username)?.toUserDetails()
                ?: throw UsernameNotFoundException("User $username not found")
        }
        throw IllegalArgumentException("No username provided")
    }

    private fun UserEntity.toUserDetails(): UserPrincipal {
        return UserPrincipal(toDomain(), password)
    }
}
