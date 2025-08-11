package com.lucwaw.takeday.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.lucwaw.takeday.data.database.entities.RowEntity

@Dao
interface RowDao {
    @Query("SELECT * FROM `rows`")
    suspend fun getAll(): List<RowEntity>

    @Upsert
    suspend fun upsert(row: RowEntity)

    @Delete
    suspend fun delete(row: RowEntity)
}