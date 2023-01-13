package at.downdrown.diary.backend.persistence.repository

import at.downdrown.diary.backend.persistence.entity.MoodCheckInEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface MoodCheckInRepository : CrudRepository<MoodCheckInEntity, Long> {

    fun findMoodCheckInEntitiesByUserUsername(username: String): List<MoodCheckInEntity>

    fun existsMoodCheckInEntitiesByUserUsernameAndCheckInPointBetween(username: String, from: LocalDateTime, to: LocalDateTime): Boolean

}
