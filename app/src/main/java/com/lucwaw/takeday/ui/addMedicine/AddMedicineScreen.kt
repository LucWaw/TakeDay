package com.lucwaw.takeday.ui.addMedicine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucwaw.takeday.R


@Composable
fun AddMedicineScreenRoot(goBack: () -> Boolean) {
    val viewModel = hiltViewModel<AddMedicineViewModel>()
    AddMedicineScreen(
        state = viewModel.state,
        goBack = goBack,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicineScreen(
    state: MedicinesState,
    onEvent: (AddMedicineEvent) -> Unit,
    goBack: () -> Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.add_medicine))
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp), // Added padding around the column content
            horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
            verticalArrangement = Arrangement.spacedBy(16.dp) // Add space between elements
        ) {
            // Simple Form with a unique text field to add a medicine
            // Button to register the medicine
            // Button disabled if the text field is empty or the medicine already exists
            TextField(
                value = state.medicineName,
                onValueChange = { newName ->
                    onEvent(AddMedicineEvent.MedicineChanged(newName))
                },
                label = { Text(stringResource(R.string.medicine_name)) },
                placeholder = { Text(stringResource(R.string.enter_medicine_name)) },
                singleLine = true,
                isError = state.error,
                modifier = Modifier
            )
            Button(
                onClick = { onEvent(AddMedicineEvent.SendMedicine); goBack() },
                enabled = !state.error && state.medicineName.isNotBlank(),
                modifier = Modifier
            ) {
                Text(stringResource(R.string.add))
            }
        }
    }
}