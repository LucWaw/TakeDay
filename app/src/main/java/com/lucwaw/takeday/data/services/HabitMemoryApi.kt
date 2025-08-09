package com.lucwaw.takeday.data.services

import com.lucwaw.takeday.data.Dailies
import com.lucwaw.takeday.data.Habit
import com.lucwaw.takeday.data.Medicine
import com.lucwaw.takeday.data.Take
import java.sql.Date
import java.time.LocalDate
import java.time.LocalTime

class DailyHabitFakeAPI : DailiesAPI {
    val firstMedicine = Medicine("name1")
    val secondMedicine = Medicine("name2")
    var firstTake = Take(firstMedicine, wasTaken = false)
    var secondTake = Take(secondMedicine, wasTaken = false)
    var habit = Habit(firstTake to secondTake, time = LocalTime.now().toString())
    var dailyHabit = Dailies(
        date = LocalDate.now().toString(),
        habit = habit
    )

    var listOFDailies = listOf(dailyHabit)

    override suspend fun getDailies(): List<Dailies> {
        return listOFDailies
    }

    override suspend fun getDaily(date: String): Dailies? {
        return if (date == "0") {
            dailyHabit
        } else {
            null
        }
    }

    override suspend fun addDaily(daily: Dailies): Dailies {
        listOFDailies = listOFDailies + dailyHabit
        return dailyHabit
    }

    override suspend fun updateDailyFromDate(
        date: String,
        dailyUpdated: Dailies
    ): Dailies {
        val dailyToUpdate = listOFDailies.find { it.date == date }
            ?: throw NoSuchElementException("No daily found for date: $date")
        listOFDailies = listOFDailies - dailyToUpdate + dailyUpdated
        return dailyUpdated
    }

    override suspend fun deleteDaily(date: String) {
        listOFDailies = listOFDailies.filterNot { it.date == date }
    }

    override suspend fun deleteAllDaily() {
        listOFDailies = emptyList()
    }


}