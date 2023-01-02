package at.downdrown.diary.backend.user

import at.downdrown.diary.api.user.UserService

class UserServiceImpl: UserService {
    override fun sayHello(hello: String) {
        println("hello $hello")
    }
}