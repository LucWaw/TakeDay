package com.lucwaw.takeday.ui.selectMedicines

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucwaw.takeday.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMedicines(goBack: () -> Unit, goToAddMedicine: () -> Unit) {
    val viewModel = hiltViewModel<SelectMedicinesViewModel>()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.select_medicines)) },
            navigationIcon =
                {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                }
        )
    }
    ) { innerPadding ->
        SelectMedicinesScreen(
            modifier = Modifier.padding(innerPadding),
            goToAddMedicine = goToAddMedicine,
            event = viewModel::onEvent,
            state = viewModel.uiState.value,
        )
    }
}

@Composable
fun SelectMedicinesScreen(
    modifier: Modifier = Modifier,
    goToAddMedicine: () -> Unit,
    state: SelectMedicinesState,
    event: (SelectMedicinesEvent) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(state.medicines.size){ index ->
            val medicine = state.medicines.keys.elementAt(index)
            MedicineItem(
                medicine = medicine,
                isSelected = state.medicines[medicine] == true,
                onClick = { event(SelectMedicinesEvent.MedicineToggle(medicine)) }
            )
        }
    }
}

@Composable
fun MedicineItem(
    medicine: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    TODO("Not yet implemented")
}