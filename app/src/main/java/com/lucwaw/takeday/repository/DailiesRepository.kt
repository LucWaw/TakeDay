package com.lucwaw.takeday.repository

import com.lucwaw.takeday.data.services.DailiesAPI
import com.lucwaw.takeday.domain.model.Dailies
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailiesRepository @Inject constructor(private val dailiesAPI: DailiesAPI) {
    suspend fun getDailies(): List<Dailies> =
        //for each daily, convert it to domain model
        dailiesAPI.getDailies().map { it.toDomain() }

    suspend fun getDaily(date: String): Dailies? = dailiesAPI.getDaily(date)?.toDomain()

    suspend fun addDaily(daily: Dailies): Dailies =
        dailiesAPI.addDaily(daily.toDAO()).toDomain()

    suspend fun updateDailyFromDate(
        date: String,
        dailyUpdated: Dailies
    ): Dailies = dailiesAPI.updateDailyFromDate(date, dailyUpdated.toDAO()).toDomain()

    suspend fun deleteDaily(date: String) {
        dailiesAPI.deleteDaily(date)
    }

    suspend fun deleteAllDaily() {
        dailiesAPI.deleteAllDaily()
    }

}