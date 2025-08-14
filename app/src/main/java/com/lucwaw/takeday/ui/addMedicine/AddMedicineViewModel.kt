package com.lucwaw.takeday.ui.addMedicine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.takeday.domain.model.Medicine
import com.lucwaw.takeday.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

sealed interface AddMedicineEvent {
    data class MedicineChanged(val medicine: Medicine) : AddMedicineEvent
    data object SendMedicine : AddMedicineEvent
}


@HiltViewModel
class AddMedicineViewModel @Inject constructor(private val repository: TableRepository) :
    ViewModel() {
    var state by mutableStateOf(
        MedicinesState(
            medicine = Medicine(
                id = 0,// Generate ID will be handled by the database
                name = "",
                isSelected = true
            )
        )
    )
        private set

    init {
        viewModelScope.launch {
            state.medicines = repository.getAllMedicines().map {
                Medicine(it.id, it.name, it.isSelected)
            }
        }
    }

    fun onEvent(event: AddMedicineEvent) {
        when (event) {
            is AddMedicineEvent.SendMedicine -> {
                viewModelScope.launch {

                    repository.insertMedicine(state.medicine)
                    state = state.copy(
                        medicines = state.medicines + state.medicine,
                        error = false
                    )
                }
            }

            is AddMedicineEvent.MedicineChanged -> {
                state.error = event.medicine.name.isBlank() || state.medicines.any { it.name == event.medicine.name }
                state = state.copy(medicine = event.medicine)
            }

        }
    }
}