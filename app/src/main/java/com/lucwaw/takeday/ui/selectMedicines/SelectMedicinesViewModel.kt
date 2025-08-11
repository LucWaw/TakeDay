package com.lucwaw.takeday.ui.selectMedicines

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lucwaw.takeday.ui.dailies.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class SelectMedicinesEvent {
    data class MedicineToggle(val medicineName: String) : SelectMedicinesEvent()
    data class DeleteMedicine(val medicineName: String) : SelectMedicinesEvent()
}

@HiltViewModel
class SelectMedicinesViewModel @Inject constructor(
    private val repository: SelectMedicinesRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(
        SelectMedicinesState()
    )

    val uiState: State<SelectMedicinesState> = _uiState

    init {
        loadMedicines()
    }

    private fun loadMedicines() {
        _uiState.value = _uiState.value.copy(
            medicines = repository.getAllMedicines().map { it.name },
        )
    }

    fun onEvent(event: SelectMedicinesEvent) {
        when (event) {
            is SelectMedicinesEvent.MedicineSelected -> {
                repository.addMedicine(event.medicineName)
                loadMedicines()
            }
            is SelectMedicinesEvent.DeleteMedicine -> {
                repository.removeMedicine(event.medicineName)
                loadMedicines()
            }
        }
    }
}