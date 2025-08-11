package com.lucwaw.takeday.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lucwaw.takeday.domain.model.Selected

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey
    val name: String,
    val isSelected: Boolean
)