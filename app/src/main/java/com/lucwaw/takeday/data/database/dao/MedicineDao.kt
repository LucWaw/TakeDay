package com.lucwaw.takeday.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lucwaw.takeday.data.database.entities.MedicineEntity

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines ORDER BY name ASC")
    suspend fun getAll(): List<MedicineEntity>

    @Query("UPDATE medicines SET isSelected = :isSelected WHERE id = :id")
    suspend fun updateIsSelected(id: Long, isSelected: Boolean)

    @Query("UPDATE medicines SET name = :name WHERE id = :id")
    suspend fun updateName(id: Long, name: String)

    @Insert
    suspend fun insert(medicine: MedicineEntity)

    @Delete
    suspend fun delete(medicine: MedicineEntity)

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getById(id: Long): MedicineEntity?
}
