package com.lucwaw.takeday.ui.draw

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucwaw.takeday.domain.usecase.GenerateCompleteRowsUseCase
import com.lucwaw.takeday.repository.TableRepository
import com.lucwaw.takeday.ui.dailies.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawScreenViewModel @Inject constructor(
    private val generateCompleteRowsUseCase: GenerateCompleteRowsUseCase,
    private val repository: TableRepository) : ViewModel() {
    private val _uiState = mutableStateOf(
        UiGraphState()
    )

    val uiState: State<UiGraphState> = _uiState

    fun updateTable() {
        viewModelScope.launch {
            _uiState.value = UiGraphState(isLoading = true)

            val rows = repository.getAllRows()
            // Create rows for each day from the first row to today, including today
            val completeRows = generateCompleteRowsUseCase(rows)

            _uiState.value = UiGraphState(
                table = completeRows,
                isLoading = false
            )
        }
    }
}