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
    data class MedicineToggle(val medicineName: String) : SelectMedicinesEvent()
    data class DeleteMedicine(val medicineName: String) : SelectMedicinesEvent()
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
                val currentMedicines = _uiState.value.medicines
                val currentSelection =
                    currentMedicines.find { it.name == event.medicineName }?.isSelected == true
                val newSelection = !currentSelection
                val updatedMedicines = currentMedicines.map { medicine ->
                    if (medicine.name == event.medicineName) {
                        medicine.copy(isSelected = newSelection)
                    } else {
                        medicine
                    }
                }

                _uiState.value = _uiState.value.copy(
                    medicines = updatedMedicines
                )
                viewModelScope.launch {
                    repository.upsertMedicine(
                        Medicine(
                            name = event.medicineName,
                            isSelected = newSelection,
                            id = currentMedicines.find { it.name == event.medicineName }?.id ?: -1
                        )
                    )
                    loadSelectedMedicines()
                }
            }

            is SelectMedicinesEvent.DeleteMedicine -> {
                val currentMedicines = _uiState.value.medicines
                val currentMedicine =
                    currentMedicines.find { it.name == event.medicineName }
                viewModelScope.launch {
                    repository.removeMedicine(currentMedicine ?: Medicine(name = event.medicineName, isSelected = false, id = -1))
                    loadSelectedMedicines()
                }

            }

        }
    }
}