package com.lucwaw.takeday.data.services

import java.time.LocalDate
import java.time.LocalTime

/*class DailyHabitFakeAPI : DailiesAPI {
    val firstMedicine = Medicine("Abilify")
    val secondMedicine = Medicine("Fluoxétine")
    var firstTake = Take(firstMedicine, wasTaken = false)
    var secondTake = Take(secondMedicine, wasTaken = false)
    var habit = Habit(listOf(firstTake, secondTake), time = LocalTime.now().toString())
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
        println("Updated medicine: $listOFDailies")

        return dailyUpdated
    }

    override suspend fun deleteDaily(date: String) {
        listOFDailies = listOFDailies.filterNot { it.date == date }
    }

    override suspend fun deleteAllDaily() {
        listOFDailies = emptyList()
    }


}*/