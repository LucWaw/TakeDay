package com.lucwaw.takeday.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val isSelected: Boolean
)