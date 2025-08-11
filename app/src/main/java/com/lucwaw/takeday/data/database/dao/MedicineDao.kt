package com.lucwaw.takeday.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucwaw.takeday.data.database.entities.MedicineEntity

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines ORDER BY name ASC")
    suspend fun getAll(): List<MedicineEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(medicine: MedicineEntity): Long

    @Delete
    suspend fun delete(medicine: MedicineEntity)
}