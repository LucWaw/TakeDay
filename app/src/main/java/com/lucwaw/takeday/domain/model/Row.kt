package com.lucwaw.takeday.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Row(
    val date: LocalDate,
    val time: LocalTime?,
    val medicines: Map<String, TriState>
)
