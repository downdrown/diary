package at.downdrown.diary.api.mood

import java.time.LocalDateTime

data class MoodCheckIn (
    var id: Long?,
    var score: MoodScore,
    var checkInPoint: LocalDateTime,
    var comment: String?,
    var createdAt: LocalDateTime?
)
