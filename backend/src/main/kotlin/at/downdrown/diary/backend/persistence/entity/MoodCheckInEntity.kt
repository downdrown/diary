package at.downdrown.diary.backend.persistence.entity

import at.downdrown.diary.api.mood.MoodScore
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "diary_mood_check_in")
class MoodCheckInEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id", nullable = false)
    var user: UserEntity?,

    @Convert(converter = MoodScoreConverter::class)
    @Column(name = "score", nullable = false)
    var score: MoodScore,

    @Column(name = "check_in_point", nullable = false)
    var checkInPoint: LocalDateTime,

    @Column(name = "comment", length = 5000)
    var comment: String?,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime?

)
