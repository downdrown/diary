package at.downdrown.diary.backend.persistence.entity

import at.downdrown.diary.api.mood.MoodScore
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class MoodScoreConverter : AttributeConverter<MoodScore, Int> {
    override fun convertToDatabaseColumn(moodScore: MoodScore?): Int? {
        return moodScore?.score
    }

    override fun convertToEntityAttribute(moodScore: Int?): MoodScore? {
        return moodScore?.let { score -> MoodScore from score }
    }
}
