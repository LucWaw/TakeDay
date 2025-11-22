package com.lucwaw.takeday.ui.draw

import com.lucwaw.takeday.domain.model.Row

data class UiGraphState(
    val table: List<Row> = emptyList(),
    val isLoading: Boolean = true
)