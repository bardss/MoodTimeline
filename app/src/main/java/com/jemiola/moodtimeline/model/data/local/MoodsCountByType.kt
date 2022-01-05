package com.jemiola.moodtimeline.model.data.local

import com.jemiola.moodtimeline.model.data.local.CircleMoodBO.*

data class MoodsCountByType(
    val veryGood: Int = 0,
    val good: Int = 0,
    val mediocre: Int = 0,
    val bad: Int = 0,
    val veryBad: Int = 0
) {
    fun getSum() = veryGood + good + mediocre + bad + veryBad
}

fun List<TimelineMoodBOv2>.toMoodsCountByType(): MoodsCountByType {
    var veryGood = 0
    var good = 0
    var mediocre = 0
    var bad = 0
    var veryBad = 0
    forEach {
        when (it.circleMood) {
            VERY_GOOD -> veryGood++
            GOOD -> good++
            MEDIOCRE -> mediocre++
            BAD -> bad++
            VERY_BAD -> veryBad++
            NONE -> { }
        }
    }
    return MoodsCountByType(veryGood, good, mediocre, bad, veryBad)
}