package com.lucwaw.takeday.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lucwaw.takeday.data.database.dao.MedicineDao
import com.lucwaw.takeday.data.database.dao.RowDao
import com.lucwaw.takeday.data.database.entities.MedicineEntity
import com.lucwaw.takeday.data.database.entities.RowEntity

@Database(entities = [RowEntity::class, MedicineEntity::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rowDao(): RowDao
    abstract fun medicineDao(): MedicineDao
}