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
            // Create rows for each day from the first row to today, including today
            val completeRows = createUnExistingRowsFromUsageBeginningToToDay(rows)

            val medicineHeaders =
                repository.getAllMedicines().filter { it.isSelected }.map { it.name }

            _uiState.value = UiState(
                headers = listOf("Date", "Time") + medicineHeaders,
                table = completeRows
            )
        }
    }

    //TODO RECALL COMPLETE AT LAUC SCREEN

    fun loadMedicines() {
        viewModelScope.launch {
            val medicineHeaders =
                repository.getAllMedicines().filter { it.isSelected }.map { it.name }
            _uiState.value = _uiState.value.copy(
                headers = listOf("Date", "Time") + medicineHeaders
            )
        }
    }



    /**
     * Creates a list of [Row] objects, filling in any missing dates between the earliest date
     * present in the input `rows` and today's date.
     *
     * If a date within this range is not found in the input `rows`, a new [Row] is created for that date
     * with `null` time and an empty map for medicines.
     *
     * If the input `rows` is empty, it will generate rows from today up to today (i.e., a single row for today).
     *
     * @param rows A list of existing [Row] objects, potentially with missing dates.
     * @return A new list of [Row] objects that includes all dates from the earliest date in the input `rows`
     * (or today if `rows` is empty) up to and including today. Existing rows are preserved, and missing
     * dates are filled with default [Row] objects.
     */
    fun createUnExistingRowsFromUsageBeginningToToDay(rows: List<Row>): List<Row> {
        val today = LocalDate.now()
        val rowsByDate = rows.associateBy { it.date }
        val startDate = rows.minOfOrNull { it.date } ?: today

        return (0..today.toEpochDay() - startDate.toEpochDay()).reversed() // Include today
            .map { startDate.plusDays(it) }
            .map { currentDate ->
                rowsByDate[currentDate] ?: Row(
                    date = currentDate,
                    time = null,
                    medicines = emptyMap() // No medicine
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
