package com.lucwaw.takeday.ui.dailies

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.takeday.repository.DailiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime


sealed interface DailiesEvent {
    data class TakeFirstMedicine(val date: LocalDate, val isTaken : Boolean) : DailiesEvent
    data class TakeSecondMedicine(val date: LocalDate, val isTaken : Boolean) : DailiesEvent
    data class PutTakeTime(val date: LocalDate, val takeTime: LocalTime) : DailiesEvent
}

@HiltViewModel
class DailiesViewModel @Inject constructor(private val repository: DailiesRepository) :
    ViewModel() {
    var state by mutableStateOf(DailiesState())
        private set

    init {
        viewModelScope.launch {
            state.dailies = repository.getDailies()
        }
    }

    fun onEvent(event: DailiesEvent) {
        when (event) {
            is DailiesEvent.TakeFirstMedicine -> {
                val dailyIndex = state.dailies.indexOfFirst { it.date == event.date }
                state.dailies[dailyIndex].habit.take.first.setIsTaken(event.isTaken)
                viewModelScope.launch {
                    val updatedOne = repository.updateDailyFromDate(event.date.toString(), state.dailies[dailyIndex])
                    state.copy(
                        dailies = state.dailies.mapIndexed { index, daily ->
                            if (index == dailyIndex) updatedOne else daily
                        }
                    )
                }
            }

            is DailiesEvent.TakeSecondMedicine -> {
                val dailyIndex = state.dailies.indexOfFirst { it.date == event.date }
                state.dailies[dailyIndex].habit.take.second.setIsTaken(event.isTaken)
                viewModelScope.launch {
                    val updatedOne = repository.updateDailyFromDate(event.date.toString(), state.dailies[dailyIndex])
                    state.copy(
                        dailies = state.dailies.mapIndexed { index, daily ->
                            if (index == dailyIndex) updatedOne else daily
                        }
                    )
                }
            }

            is DailiesEvent.PutTakeTime -> {
                val dailyIndex = state.dailies.indexOfFirst { it.date == event.date }
                state.dailies[dailyIndex].habit.time = event.takeTime
                viewModelScope.launch {
                    val updatedOne = repository.updateDailyFromDate(event.date.toString(), state.dailies[dailyIndex])
                    state.copy(
                        dailies = state.dailies.mapIndexed { index, daily ->
                            if (index == dailyIndex) updatedOne else daily
                        }
                    )
                }
            }
        }
    }
}