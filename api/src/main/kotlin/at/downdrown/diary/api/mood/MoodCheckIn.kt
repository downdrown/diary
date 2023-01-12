package at.downdrown.diary.api.mood

import java.time.LocalDate
import java.time.LocalDateTime

data class MoodCheckIn (
    var id: Long?,
    var score: MoodScore,
    var checkInDate: LocalDate,
    var comment: String?,
    var createdAt: LocalDateTime?
)
