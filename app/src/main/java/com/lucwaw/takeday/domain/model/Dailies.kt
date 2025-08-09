package com.lucwaw.takeday.domain.model

import com.lucwaw.takeday.data.Dailies
import java.time.LocalDate

data class Dailies(val date : LocalDate = LocalDate.now(), val habit: Habit){
    fun toDAO() : Dailies {
        return Dailies(date.toString(), habit.toDAO())
    }
}