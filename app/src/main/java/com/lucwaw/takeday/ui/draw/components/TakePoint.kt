package com.lucwaw.takeday.ui.draw.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lucwaw.takeday.domain.model.Row
import com.lucwaw.takeday.ui.draw.components.TimeGraphScope.timeGraphPoint
import java.time.LocalDateTime


@Composable
fun TakePoint(data: Row, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier// Use your new modifier
            .timeGraphPoint(
                pointOfTime = LocalDateTime.of(data.date, data.time), // Your actual data point time
                hours = (8..20).toList()
            )
            .size(8.dp) // Give the point a size
            .background(Color.Red, CircleShape) //TODO UPDATE COLOR WITH WANTED
    )
}