package at.downdrown.diary.backend.mood

import at.downdrown.diary.api.mood.MoodCheckIn
import at.downdrown.diary.backend.persistence.entity.MoodCheckInEntity

fun MoodCheckIn.toEntity(): MoodCheckInEntity {
    return MoodCheckInEntity(
        id = id,
        user = null,
        score = score,
        checkInDate = checkInDate,
        comment = comment,
        createdAt = null
    )
}

fun MoodCheckInEntity.toDomain(): MoodCheckIn {
    return MoodCheckIn(
        id = id,
        score = score,
        checkInDate = checkInDate,
        comment = comment,
        createdAt = createdAt
    )
}
