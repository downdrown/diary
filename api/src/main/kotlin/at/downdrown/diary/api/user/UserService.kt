package at.downdrown.diary.api.user

interface UserService {

    fun register(user: User, password: String)

    fun update(user: User)

    fun delete(userId: Long)

    fun findByUsername(username: String): User?

    fun exists(username: String): Boolean

    fun changePassword(username: String, currentPassword: String, newPassword: String)

    fun updateLastLogin(username: String)

}