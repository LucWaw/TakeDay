package com.lucwaw.takeday.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.lucwaw.takeday.data.database.entities.MedicineEntity

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines ORDER BY name ASC")
    suspend fun getAll(): List<MedicineEntity>

    @Upsert
    suspend fun upsert(medicine: MedicineEntity)

    @Query("Delete FROM medicines WHERE name = :medicineName")
    suspend fun deleteByName(medicineName: String)
}