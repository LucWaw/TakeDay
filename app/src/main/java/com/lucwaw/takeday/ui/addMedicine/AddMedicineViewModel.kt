package com.lucwaw.takeday.ui.addMedicine

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.takeday.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

sealed interface AddMedicineEvent {
    data class MedicineChanged(val medicineName: String) : AddMedicineEvent
    data object SendMedicine : AddMedicineEvent
}


@HiltViewModel
class AddMedicineViewModel @Inject constructor(private val repository: TableRepository) : ViewModel() {
    var state by mutableStateOf(MedicinesState())
        private set

    init {
        viewModelScope.launch {
            state.medicines = repository.getAllMedicines().map {
                it.name
            }
        }
    }

    fun onEvent(event: AddMedicineEvent) {
        when (event) {
            is AddMedicineEvent.SendMedicine -> {
                viewModelScope.launch {

                    repository.upsertMedicine(state.medicineName)
                    state = state.copy(
                        medicines = state.medicines + state.medicineName,
                        medicineName = "",
                        error = false
                    )
                }
            }

            is AddMedicineEvent.MedicineChanged -> {
                Log.d("AddMedicineVM", "Before update: state.medicineName = '${state.medicineName}', event.medicineName = '${event.medicineName}'")
                state.error = event.medicineName.isBlank() || state.medicines.any { it == event.medicineName }
                state = state.copy(medicineName = event.medicineName)
                Log.d("AddMedicineVM", "After update: state.medicineName = '${state.medicineName}'")
            }

        }
    }
}