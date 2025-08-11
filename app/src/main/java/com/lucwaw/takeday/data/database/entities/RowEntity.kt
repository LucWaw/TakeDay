package com.lucwaw.takeday.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lucwaw.takeday.domain.model.TriState
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "rows")
data class RowEntity(
    @PrimaryKey val date: LocalDate,
    val time: LocalTime?,
    val medicines: Map<String, TriState>
)