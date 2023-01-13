package at.downdrown.diary.api.mood

enum class MoodScore(val score: Int) {

    SuperGood(5),
    Good(4),
    Indifferent(3),
    Bad(2),
    VeryBad(1);

    companion object {
        infix fun from(moodScore: Int): MoodScore? = MoodScore.values().firstOrNull { it.score == moodScore }
    }
}
