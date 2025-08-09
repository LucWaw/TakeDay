package com.lucwaw.takeday.data.services

import com.lucwaw.takeday.data.Dailies
import java.sql.Date

interface DailiesAPI {
    suspend fun getDailies(): List<Dailies>

    suspend fun getDaily(date: String): Dailies?

    suspend fun addDaily(daily: Dailies): Dailies

    suspend fun updateDailyFromDate(
        date: String,
        dailyUpdated: Dailies
    ): Dailies

    suspend fun deleteDaily(date: String)

    suspend fun deleteAllDaily()
}