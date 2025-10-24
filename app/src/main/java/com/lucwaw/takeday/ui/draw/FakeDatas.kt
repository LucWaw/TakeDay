package com.lucwaw.takeday.ui.draw

import com.lucwaw.takeday.domain.model.TriState
import java.time.LocalTime

val fakeGraphData: List<Take> = listOf(
    Take(
        secondOfDay = LocalTime.of(12, 10).toSecondOfDay(),
        listOf(
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.EMPTY
        )
    ),
    Take(
        secondOfDay = LocalTime.of(13, 30).toSecondOfDay(),
        listOf(
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(18, 50).toSecondOfDay(),
        listOf(
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.EMPTY,
            TriState.TAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(19, 5).toSecondOfDay(),
        listOf(
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(20, 25).toSecondOfDay(),
        listOf(
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(21, 0).toSecondOfDay(),
        listOf(
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY
        )
    ),
    Take(
        secondOfDay = LocalTime.of(22, 15).toSecondOfDay(),
        listOf(
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.EMPTY
        )
    ),
    Take(
        secondOfDay = LocalTime.of(23, 45).toSecondOfDay(),
        listOf(
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(23, 30).toSecondOfDay(),
        listOf(
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN,
            TriState.NOTTAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(18, 0).toSecondOfDay(),
        listOf(
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.EMPTY,
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.TAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(19, 5).toSecondOfDay(),
        listOf(
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN
        )
    ),
    Take(
        secondOfDay = LocalTime.of(18, 55).toSecondOfDay(),
        listOf(
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY,
            TriState.EMPTY
        )
    ),
    Take(
        secondOfDay = LocalTime.of(18, 35).toSecondOfDay(),
        listOf(
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.TAKEN,
            TriState.NOTTAKEN,
            TriState.TAKEN,
            TriState.EMPTY
        )
    )
)