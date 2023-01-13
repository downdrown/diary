package at.downdrown.diary.backend.persistence.entity

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "diary_users")
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 500, unique = true)
    var username: String,

    @Column(nullable = false, length = 1000)
    var firstname: String,

    @Column(nullable = false, length = 1000)
    var lastname: String,

    @Column
    var birthdate: LocalDate?,

    @Column(nullable = false, length = 1000)
    var email: String,

    @Column(nullable = false)
    var registeredAt: LocalDateTime,

    @Column
    var lastLoginAt: LocalDateTime?,

    @Column(nullable = false, length = 1000)
    var password: String,

    @OneToMany(mappedBy = "user", cascade = [ CascadeType.REMOVE ], fetch = FetchType.LAZY)
    var moodCheckIns: List<MoodCheckInEntity>

)
