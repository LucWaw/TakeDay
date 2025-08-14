package com.lucwaw.takeday.ui.selectMedicines

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.takeday.domain.model.Medicine
import com.lucwaw.takeday.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SelectMedicinesEvent {
    data class MedicineToggle(val medicineId: Long, val isSelected: Boolean) : SelectMedicinesEvent()
    data class DeleteMedicine(val medicine: Medicine) : SelectMedicinesEvent()
}

@HiltViewModel
class SelectMedicinesViewModel @Inject constructor(
    private val repository: TableRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(
        SelectMedicinesState()
    )

    val uiState: State<SelectMedicinesState> = _uiState

    init {
        loadSelectedMedicines()
    }

    fun loadSelectedMedicines() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                medicines = repository.getAllMedicines()
            )
        }

    }

    fun onEvent(event: SelectMedicinesEvent) {
        when (event) {
            is SelectMedicinesEvent.MedicineToggle -> {
                viewModelScope.launch {
                    repository.updateMedicineSelectionFromId(
                        medicineId = event.medicineId,
                        isSelected = event.isSelected
                    )
                    loadSelectedMedicines()
                }
            }

            is SelectMedicinesEvent.DeleteMedicine -> {
                viewModelScope.launch {
                    repository.removeMedicine(event.medicine)
                    loadSelectedMedicines()
                }

            }

        }
    }
}