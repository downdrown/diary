package at.downdrown.diary.api.mood

import java.time.LocalDate
import java.time.LocalDateTime

data class MoodCheckIn (
    var id: Long?,
    var moodScore: MoodScore,
    var moodDate: LocalDate,
    var moodStatement: String?,
    var createdAt: LocalDateTime?
)
