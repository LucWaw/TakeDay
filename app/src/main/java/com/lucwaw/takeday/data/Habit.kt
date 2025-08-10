package com.lucwaw.takeday.data

data class Habit(val take: Pair<Take, Take>, val time: String) {
    fun toDomain(): com.lucwaw.takeday.domain.model.Habit {
        return com.lucwaw.takeday.domain.model.Habit(
            take = take.first.toDomain() to take.second.toDomain()
        ).apply {
            time = java.time.LocalTime.parse(this@Habit.time)
        }
    }
}
