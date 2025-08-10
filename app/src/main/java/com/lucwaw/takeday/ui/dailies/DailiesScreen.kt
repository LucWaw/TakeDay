package com.lucwaw.takeday.ui.dailies

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucwaw.takeday.R
import com.lucwaw.takeday.domain.model.Dailies
import com.lucwaw.takeday.ui.theme.DarkGrey
import com.lucwaw.takeday.ui.theme.Pink
import com.lucwaw.takeday.utils.DateUtils.Companion.toHumanDate
import com.lucwaw.takeday.utils.TimeUtils.Companion.toHumanTime
import java.time.LocalDate

@Composable
fun DailiesScreenRoot(goToAddScreen: () -> Unit) {
    val viewModel = hiltViewModel<DailiesViewModel>()
    DailiesScreen(
        state = viewModel.state,
        gotToAddScreen = goToAddScreen,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailiesScreen(
    state: DailiesState,
    gotToAddScreen: () -> Unit,
    onEvent: (DailiesEvent) -> Unit
) {
    val dailies = state.dailies[0]
    val headers = listOf(
        "",
        dailies.habit.take.first.medicine.name,
        dailies.habit.take.second.medicine.name,
        "Time"
    )
    val data = List(1) { index ->
        listOf(
            dailies.date.toHumanDate(),
            dailies.habit.take.first.medicine.name,
            dailies.habit.take.second.medicine.name,
            dailies.habit.time.toHumanTime()
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            Log.d("NAVIGATION", "Navigating to AddMedicine")
                            gotToAddScreen()
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(R.string.add_a_medicine),
                            tint = Color.White
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        Table(
            headers = headers,
            data = data,
            paddingValues = innerPadding,
            onEvent = onEvent,
            state = state
        )
    }
}

@Composable
fun TableHeader(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    headers: List<String>
) {
    Row(
        modifier = modifier
            .horizontalScroll(scrollState)
            .background(Pink)
    ) {
        headers.forEach { header ->
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = header,
                    color = DarkGrey,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
    }
}

@Composable
fun TableContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    data: List<List<String>>,
    onEvent: (DailiesEvent) -> Unit,
    state: DailiesState
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(data) { row ->
            Row(modifier = Modifier.horizontalScroll(scrollState)) {
                row.forEach { cell ->
                    Box(
                        modifier = Modifier
                            .width(110.dp)
                            .padding(8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        //TODO verify if cell name is a medicine in base
                        if (cell == "Abilify" || cell == "Fluoxétine") {
                            var checked by remember { mutableStateOf(false) }

                            Checkbox(
                                checked = checked,//TODO FROM STATE
                                onCheckedChange = { checked = it;
                                    if (cell == "Abilify") {
                                        onEvent(DailiesEvent.TakeFirstMedicine(state.dailies[0].date, it))
                                    } else if (cell == "Fluoxétine") {
                                        onEvent(DailiesEvent.TakeSecondMedicine(state.dailies[0].date, it))
                                    }
                                },
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            Text(
                                text = cell,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                    }


                }
            }
        }
    }
}

@Composable
fun Table(
    modifier: Modifier = Modifier,
    headers: List<String>,
    data: List<List<String>>,
    paddingValues: PaddingValues,
    onEvent: (DailiesEvent) -> Unit,
    state: DailiesState
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        TableHeader(scrollState = scrollState, headers = headers)
        TableContent(scrollState = scrollState, data = data, onEvent = onEvent, state = state)

    }
}

/**
 * This function create a map of medicines with the name of the medicine as key and a list of pair of
 * LocalDate and take Boolean as value.
 * This is in bussiness term a "take map" that will be used to show the medicines taken in a specific date.
 */
fun createTakeMap(list: List<Dailies>) : Map<String, List<Pair<LocalDate, Boolean>>> {
    val takeMap = mutableMapOf<String, MutableList<Pair<LocalDate, Boolean>>>()

    list.forEach { daily ->
        daily.habit.take.first.medicine.name.let { medicineName ->
            takeMap.getOrPut(medicineName) { mutableListOf() }
                .add(Pair(daily.date, daily.habit.take.first.wasTaken))
        }

        daily.habit.take.second.medicine.name.let { medicineName ->
            takeMap.getOrPut(medicineName) { mutableListOf() }
                .add(Pair(daily.date, daily.habit.take.second.wasTaken))
        }
    }

    return takeMap

}