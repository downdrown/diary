package at.downdrown.diary.backend.config

import at.downdrown.diary.api.user.User
import at.downdrown.diary.api.user.UserService
import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import jakarta.annotation.PostConstruct

private val log = KotlinLogging.logger {}

@Service
@Profile("dev")
class BackendDevAutoSetup(
    private val userService: UserService
) {

    private val defaultUser = User(
        null,
        "user",
        "Firstname",
        "Lastname",
        LocalDate.now(),
        "user@testmail.com",
        LocalDateTime.now(),
        null
    )
    private val defaultPassword = "password"

    @PostConstruct
    fun init() {
        log.info { "Initializing BackendDevAutoSetup ..." }
        initDevUsers()
    }

    private fun initDevUsers() {
        log.info { "Creating dev users ..." }
        if (!userService.exists(defaultUser.username)) {
            userService.register(defaultUser,defaultPassword)
            log.info { "Success! Dev users created." }
        }
        log.info { "Users already created." }
    }
}
