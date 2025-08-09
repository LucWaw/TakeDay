package com.lucwaw.takeday.data

import com.lucwaw.takeday.domain.model.Dailies
import java.time.LocalDate

data class Dailies(val date : String, val habit: Habit){
    fun toDomain() : com.lucwaw.takeday.domain.model.Dailies {
        return com.lucwaw.takeday.domain.model.Dailies(
            date = LocalDate.parse(date),
            habit = habit.toDomain()
        )
    }
}