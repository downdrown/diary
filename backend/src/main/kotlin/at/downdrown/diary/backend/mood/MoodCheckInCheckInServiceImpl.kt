package at.downdrown.diary.backend.mood

import at.downdrown.diary.api.mood.MoodCheckIn
import at.downdrown.diary.api.mood.MoodCheckInService
import at.downdrown.diary.backend.persistence.currentUserReference
import at.downdrown.diary.backend.persistence.repository.MoodCheckInRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import jakarta.persistence.EntityManager

@Service
class MoodCheckInCheckInServiceImpl(
    private val entityManager: EntityManager,
    private val moodCheckInRepository: MoodCheckInRepository
) : MoodCheckInService {

    override fun checkIn(mood: MoodCheckIn) {

        val moodEntity = mood.toEntity()
        moodEntity.user = entityManager.currentUserReference()
        moodEntity.createdAt = LocalDateTime.now()

        moodCheckInRepository.save(moodEntity)
    }

    override fun hasAlreadyCheckedIn(username: String, date: LocalDate): Boolean {

        val start = date.atTime(LocalTime.MIN)
        val end = date.atTime(LocalTime.MAX)

        return moodCheckInRepository.existsMoodCheckInEntitiesByUserUsernameAndCheckInPointBetween(username, start, end)
    }
}
