package com.lucwaw.takeday.ui.dailies

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.takeday.domain.model.Row
import com.lucwaw.takeday.domain.model.TriState
import com.lucwaw.takeday.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject


// Events
sealed class TableEvent {
    data class NoteMedicine(val rowIndex: Int, val medicineName: String, val note: TriState) :
        TableEvent()
    data class TimeChanged(val rowIndex: Int, val time: LocalTime) : TableEvent()
}

@HiltViewModel
class TableViewModel @Inject constructor(private val repository: TableRepository) : ViewModel() {
    private val _uiState = mutableStateOf(
        UiState()
    )

    val uiState: State<UiState> = _uiState

    init {
        viewModelScope.launch {
            val rows = repository.getAllRows()
            val today = LocalDate.now()

            // Check if today's date already exists
            val existsToday = rows.any { it.date == today }

            val newRow = if (!existsToday) {
                Row(
                    date = today,
                    time = null,
                    medicines = emptyMap() // No medicine
                ).also { repository.upsertRow(it) }
            } else null

            val currentRows = if (newRow != null) rows + newRow else rows

            val medicineHeaders = repository.getAllMedicines().filter { it.isSelected }.map { it.name }

            _uiState.value = UiState(
                headers = listOf("Date", "Time") + medicineHeaders,
                table = currentRows
            )
        }
    }

    fun loadMedicines() {
        viewModelScope.launch {
            val medicineHeaders = repository.getAllMedicines().filter { it.isSelected }.map { it.name }
            _uiState.value = _uiState.value.copy(
                headers = listOf("Date", "Time") + medicineHeaders
            )
        }
    }



    fun onEvent(event: TableEvent) {
        when (event) {
            is TableEvent.NoteMedicine -> {
                val updatedTable = _uiState.value.table.mapIndexed { index, row ->
                    if (index == event.rowIndex) {
                        val updatedMedicines = row.medicines.toMutableMap()
                        updatedMedicines[event.medicineName] =
                            event.note
                        val updatedRow = row.copy(medicines = updatedMedicines)
                        viewModelScope.launch {
                            repository.upsertRow(
                                updatedRow
                            )
                        }
                        updatedRow
                    } else row
                }
                _uiState.value = _uiState.value.copy(table = updatedTable)
            }

            is TableEvent.TimeChanged -> {
                val updatedTable = _uiState.value.table.mapIndexed { index, row ->
                    if (index == event.rowIndex) {
                        val updatedRow = row.copy(time = event.time)
                        viewModelScope.launch {
                            repository.upsertRow(
                                updatedRow
                            )
                        }
                        updatedRow
                    } else row
                }
                _uiState.value = _uiState.value.copy(table = updatedTable)
            }
        }
    }
}
