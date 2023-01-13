package at.downdrown.diary.api.mood

import java.time.LocalDate

interface MoodCheckInService {

    fun checkIn(mood: MoodCheckIn)

    fun hasAlreadyCheckedIn(username: String, date: LocalDate): Boolean

}
