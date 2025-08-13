package com.lucwaw.takeday.di

import android.content.Context
import androidx.room.Room
import com.lucwaw.takeday.data.MIGRATION_1_2
import com.lucwaw.takeday.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideUserDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, AppDatabase::class.java, "AppDatabase",
    ).addMigrations(MIGRATION_1_2)
        .build()

    @Singleton
    @Provides
    fun provideRowDao(db: AppDatabase) = db.rowDao()

    @Singleton
    @Provides
    fun provideMedicineDao(db: AppDatabase) = db.medicineDao()
}