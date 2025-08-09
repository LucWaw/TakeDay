package com.lucwaw.takeday.domain.model

import com.lucwaw.takeday.data.Habit
import java.time.LocalTime

data class Habit(val take: Pair<Take, Take>, val time: LocalTime) {
    fun toDAO(): Habit {
        return Habit(take.first.toDAO() to take.second.toDAO(), time.toString())
    }
}
