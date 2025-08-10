package com.lucwaw.takeday.di

import com.lucwaw.takeday.data.services.DailiesAPI
import com.lucwaw.takeday.data.services.DailyHabitFakeAPI
import com.lucwaw.takeday.data.services.MedicineAPI
import com.lucwaw.takeday.data.services.MedicineFakeAPI
import com.lucwaw.takeday.repository.DailiesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This class acts as a Dagger Hilt module, responsible for providing dependencies to other parts of the application.
 * It's installed in the SingletonComponent, ensuring that dependencies provided by this module are created only once
 * and remain available throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    /**
     * Provides a Singleton instance of DailiesApi using a DailyHabitFakeAPI implementation for testing purposes.
     * This means that whenever a dependency on DailiesApi is requested, the same instance of DailyHabitFakeAPI will be used
     * throughout the application, ensuring consistent data for testing scenarios.
     *
     * @return A Singleton instance of PostFakeApi.
     */
    @Provides
    @Singleton
    fun providePostApi(): DailiesAPI {
        return DailyHabitFakeAPI()
    }

    /**
     * Provides a Singleton instance of MedicineAPI using a MedicineFakeAPI implementation for testing purposes.
     * This means that whenever a dependency on MedicineAPI is requested, the same instance of MedicineFakeAPI will be used
     * throughout the application, ensuring consistent data for testing scenarios.
     * @return A Singleton instance of MedicineFakeAPI.
     */
    @Provides
    @Singleton
    fun provideMedicineApi(): MedicineAPI {
        return MedicineFakeAPI()
    }
}
