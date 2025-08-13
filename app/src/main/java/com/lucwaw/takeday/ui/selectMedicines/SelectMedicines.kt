package com.lucwaw.takeday.ui.selectMedicines

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucwaw.takeday.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMedicines(
    goBack: () -> Unit,
    goToAddMedicine: () -> Unit,
    goToMedicineDetails: (Long) -> Unit
) {
    val viewModel = hiltViewModel<SelectMedicinesViewModel>()
    LaunchedEffect(true) {
        viewModel.loadSelectedMedicines()
    }
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
            onEvent = viewModel::onEvent,
            state = viewModel.uiState.value,
            goToMedicineDetails = goToMedicineDetails
        )
    }
}

@Composable
fun SelectMedicinesScreen(
    modifier: Modifier = Modifier,
    goToAddMedicine: () -> Unit,
    state: SelectMedicinesState,
    onEvent: (SelectMedicinesEvent) -> Unit,
    goToMedicineDetails: (Long) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(state.medicines) {
            MedicineItem(
                medicine = it.name,
                isSelected = it.isSelected,
                onEvent = onEvent,
                goToMedicineDetails = { goToMedicineDetails(it.id) }
            )
        }
        item {
            TextButton(
                onClick = goToAddMedicine,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = stringResource(R.string.add_medicine))
            }
        }
    }
}

@Composable
fun MedicineItem(
    medicine: String,
    isSelected: Boolean,
    onEvent: (SelectMedicinesEvent) -> Unit,
    goToMedicineDetails: () -> Unit
) {
    var dialogDelete by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {

        Checkbox(
            checked = isSelected,
            onCheckedChange = { onEvent(SelectMedicinesEvent.MedicineToggle(medicine)) },
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = medicine,
            modifier = Modifier
                .padding(8.dp)
                .width(100.dp)
                .clickable {goToMedicineDetails()},
        )
        IconButton(
            onClick = { dialogDelete = true }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.delete_medicine)
            )
        }

        if (dialogDelete) {
            AlertDialog(
                onDismissRequest = { dialogDelete = false },
                title = { Text(text = stringResource(R.string.popup_message_confirmation_delete_medicine)) },
                text = {
                    Text(
                        text = stringResource(
                            R.string.popup_message_confirmation_delete_medicine_text,
                            medicine
                        )
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        onEvent(SelectMedicinesEvent.DeleteMedicine(medicine))
                        dialogDelete = false
                    }) {
                        Text(text = stringResource(R.string.popup_message_choice_yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { dialogDelete = false }) {
                        Text(text = stringResource(R.string.popup_message_choice_no))
                    }
                }
            )
        }

    }
}


