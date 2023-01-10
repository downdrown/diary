package at.downdrown.diary.backend.persistence.entity

import at.downdrown.diary.api.mood.MoodScore
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "diary_mood_check_in")
class MoodCheckInEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne(cascade = [ CascadeType.REMOVE ])
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    var user: UserEntity?,

    @Enumerated
    @Column(name = "mood", nullable = false)
    var moodScore: MoodScore,

    @Column(name = "mood_date", nullable = false)
    var moodDate: LocalDate,

    @Column(name = "mood_statement", length = 5000)
    var moodStatement: String?,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime?

)
