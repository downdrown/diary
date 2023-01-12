package at.downdrown.diary.backend.persistence.repository

import at.downdrown.diary.backend.persistence.entity.MoodCheckInEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MoodCheckInRepository : CrudRepository<MoodCheckInEntity, Long> {

    @Query("select m from MoodCheckInEntity m where m.user.username = ?1")
    fun findByUsername(username: String): List<MoodCheckInEntity>

}
