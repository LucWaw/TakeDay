package com.lucwaw.takeday.ui.dailies

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucwaw.takeday.R
import com.lucwaw.takeday.domain.model.TriState
import com.lucwaw.takeday.ui.theme.DarkGrey
import com.lucwaw.takeday.ui.theme.Pink
import com.lucwaw.takeday.utils.DateUtils.Companion.toHumanDate
import com.lucwaw.takeday.utils.TimeUtils.Companion.toHumanTime
import java.time.LocalTime
import java.util.Locale

@Composable
fun DailiesScreenRoot(
    goToAddScreen: () -> Unit,
    goToSelectMedicines: () -> Unit,
    goToDiagram: () -> Unit
) {
    val viewModel = hiltViewModel<TableViewModel>()
    LaunchedEffect(true) {
        viewModel.updateTable()
    }
    DailiesScreen(
        state = viewModel.uiState.value,
        gotToAddScreen = goToAddScreen,
        goToSelectMedicines = goToSelectMedicines,
        goToDiagram = goToDiagram,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailiesScreen(
    state: UiState,
    gotToAddScreen: () -> Unit,
    goToSelectMedicines: () -> Unit,
    goToDiagram: () -> Unit,
    onEvent: (TableEvent) -> Unit
) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            gotToAddScreen()
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_a_medicine),
                        )
                    }
                    IconButton(
                        onClick =
                            {
                                goToSelectMedicines()
                            }
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.splitscreen_vertical_add),
                            contentDescription = stringResource(R.string.select_medicines),
                        )
                    }
                    IconButton(
                        onClick =
                            {
                                goToDiagram()
                            }
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.data_thresholding),
                            contentDescription = stringResource(R.string.select_medicines),
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Table(
            state = state,
            onEvent = onEvent,
            innerPadding = innerPadding
        )
    }
}

@Composable
fun Table(state: UiState, onEvent: (TableEvent) -> Unit, innerPadding: PaddingValues) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {

        TableHeader(scrollState = scrollState, headers = state.headers)
        TableContent(
            state = state,
            onEvent = onEvent,
            headers = state.headers,
            scrollState = scrollState
        )

    }
}


@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    headers: List<String>
) {
    val width = 107.dp
    val modifierRow = if (headers.size > 4) {
        modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
    } else {
        modifier.fillMaxWidth()
    }



    Row(
        modifier = modifierRow
            .background(Pink)
    ) {
        val modifierBox = if (headers.size <= 4) {
            Modifier.weight(1f)
        } else {
            Modifier.width(width)
        }
        headers.forEach { header ->
            Box(
                modifier = modifierBox
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                val headerList = header.split(" ")
                if (header == "Date" || header == "Time") {
                    Text(
                        text = if (header != "Date") header else "",
                        color = DarkGrey,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Text(
                        text = headerList[0].take(4) + "\n" + headerList.getOrElse(1) { "" }
                            .take(4),
                        color = DarkGrey,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableContent(
    modifier: Modifier = Modifier,
    headers: List<String>,
    state: UiState, onEvent: (TableEvent) -> Unit, scrollState: ScrollState
) {
    val width = 107.dp


    var showDialWithTimeDialog by remember { mutableStateOf(false) }
    var indexTimePicker by remember { mutableIntStateOf(0) }
    val selectedTime = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
        is24Hour = Locale.getDefault().language == "fr"
    )



    LazyColumn(modifier = modifier) {
        items(state.table.size) { rowIndex ->
            val row = state.table[rowIndex]
            val modifierRow = if (headers.size > 4) {
                modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            } else {
                modifier.fillMaxWidth()
            }
            Row(
                modifier = modifierRow
            ) {
                val modifierItems = if (headers.size <= 4) {
                    Modifier.weight(1f)
                } else {
                    Modifier.width(width)
                }
                state.headers.forEach { header ->
                    when (header) {
                        "Date" -> Text(
                            text = row.date.toHumanDate(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = modifierItems
                                .padding(8.dp)
                                .align(Alignment.CenterVertically)
                        )

                        "Time" ->
                            Box(
                                modifier = modifierItems
                                    .padding(8.dp)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = row.time?.toHumanTime() ?: "HH:MM",
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .alpha(0f)
                                        .clickable(onClick = {
                                            showDialWithTimeDialog = true
                                            indexTimePicker = rowIndex
                                        }),
                                )

                            }

                        else -> {

                            val (toggleableState, color) = when (row.medicines[header]) {
                                TriState.TAKEN -> ToggleableState.On to Color.Green
                                TriState.NOTTAKEN -> ToggleableState.Indeterminate to Color.Red
                                else -> ToggleableState.Off to Color.Unspecified
                            }

                            TriStateCheckbox(
                                state = toggleableState,
                                onClick = {
                                    onEvent(
                                        TableEvent.NoteMedicine(
                                            rowIndex = rowIndex,
                                            medicineName = header,
                                            note = when (row.medicines[header]) {
                                                TriState.TAKEN -> TriState.NOTTAKEN
                                                TriState.NOTTAKEN -> TriState.EMPTY
                                                TriState.EMPTY -> TriState.TAKEN
                                                null -> TriState.TAKEN
                                            }
                                        )
                                    )

                                }, colors = CheckboxDefaults.colors(
                                    checkedColor = color
                                ),
                                modifier = modifierItems
                                    .padding(8.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        }
    }
    if (showDialWithTimeDialog) {
        DialWithTimeDialog(
            onDismiss = {
                showDialWithTimeDialog = false
            },
            onConfirm = {
                onEvent(
                    TableEvent.TimeChanged(
                        rowIndex = indexTimePicker,
                        time = it
                    )
                )
                showDialWithTimeDialog = false
            },
            timePickerState = selectedTime
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialWithTimeDialog(
    onConfirm: (LocalTime) -> Unit,
    timePickerState: TimePickerState,
    onDismiss: () -> Unit,
) {
    TimePickerDialog(
        onDismiss = { onDismiss() },
        onConfirm = {
            onConfirm(LocalTime.of(timePickerState.hour, timePickerState.minute))

        }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}