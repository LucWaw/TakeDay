package com.lucwaw.takeday.ui.medicineDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucwaw.takeday.R
import kotlin.math.sin

@Composable
fun MedicineDetailsScreenRoot(medicineId: Long, goBack: () -> Boolean) {
    val viewModel = hiltViewModel<MedicineDetailsViewModel>()
    LaunchedEffect(true) {
        viewModel.loadMedicine(medicineId)
    }
    MedicineDetailsScreen(
        state = viewModel.uiState.value,
        goBack = goBack,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineDetailsScreen(
    state: MedicineDetailsState,
    goBack: () -> Boolean,
    onEvent: (MedicineDetailsEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.modify_medicine))
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Medicine(
            state = state,
            onEvent = onEvent,
            goBack = goBack,
            innerPadding = innerPadding
        )
    }

}

@Composable
fun Medicine(
    state: MedicineDetailsState,
    onEvent: (MedicineDetailsEvent) -> Unit,
    goBack: () -> Boolean,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp), // Added padding around the column content
        horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
        verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between elements
    ) {
        TextField(
            value = state.medicine.name,
            isError = state.error,
            onValueChange = { modifiedName ->
                onEvent(
                    MedicineDetailsEvent.MedicineChanged(
                        state.medicine.copy(
                            name = modifiedName,
                            id = state.medicine.id,
                            isSelected = state.medicine.isSelected
                        )
                    )
                )
            },
            singleLine = true
        )
        Button(enabled = !state.error && state.medicine.name.isNotBlank(), onClick = { onEvent(MedicineDetailsEvent.SaveMedicine); goBack() }) {
            Text(stringResource(R.string.save))
        }
    }
}