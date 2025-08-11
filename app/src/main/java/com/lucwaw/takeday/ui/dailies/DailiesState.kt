package com.lucwaw.takeday.ui.dailies

import com.lucwaw.takeday.domain.model.Row

data class UiState(
    val headers: List<String> = emptyList(), // "date", "time", puis noms des médicaments
    val table: List<Row> = emptyList()
)