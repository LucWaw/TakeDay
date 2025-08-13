package com.lucwaw.takeday.ui.medicineDetails

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.takeday.domain.model.Medicine
import com.lucwaw.takeday.repository.TableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class MedicineDetailsEvent {
    data class MedicineChanged(val medicine: Medicine) : MedicineDetailsEvent()
    data object SaveMedicine : MedicineDetailsEvent()
}

@HiltViewModel
class MedicineDetailsViewModel @Inject constructor(
    private val repository: TableRepository
) : ViewModel() {
    private val _uiState = mutableStateOf(
        MedicineDetailsState(
            medicine = Medicine(
                id = -1,
                name = "",
                isSelected = false
            )
        )
    )

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(medicines = repository.getAllMedicines())
        }
    }

    fun loadMedicine(medicineId: Long) {
        viewModelScope.launch {
            val medicine = repository.getMedicineById(medicineId)
            _uiState.value = _uiState.value.copy(
                medicine = medicine ?: Medicine(
                    id = -1,
                    name = "",
                    isSelected = false
                )
            )

        }
    }

    val uiState: State<MedicineDetailsState> = _uiState


    fun onEvent(event: MedicineDetailsEvent) {
        when (event) {
            is MedicineDetailsEvent.SaveMedicine -> {
                viewModelScope.launch {
                    repository.upsertMedicine(uiState.value.medicine)
                }
            }

            is MedicineDetailsEvent.MedicineChanged -> {
                val medicine = event.medicine
                _uiState.value = _uiState.value.copy(
                    medicine = medicine,
                    error = medicine.name.isBlank() || medicine.id < 0 || _uiState.value.medicines.any { it.name == medicine.name }
                )
                Log.d("MedicineDetailsViewModel", "Medicine changed: ${medicine.name}, ID: ${medicine.id}")
            }
        }
    }
}