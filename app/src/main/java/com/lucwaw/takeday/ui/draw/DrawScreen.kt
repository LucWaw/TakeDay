package com.lucwaw.takeday.ui.draw

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lucwaw.takeday.R
import com.lucwaw.takeday.domain.model.Row
import com.lucwaw.takeday.domain.model.TriState
import com.lucwaw.takeday.ui.draw.components.TakePoint
import com.lucwaw.takeday.ui.draw.components.TimeGraph
import com.lucwaw.takeday.ui.theme.BarColor
import com.lucwaw.takeday.ui.theme.PathColor
import java.time.LocalTime

@Composable
fun DrawRoot(onBackClick: () -> Boolean) {
    val viewModel = hiltViewModel<DrawScreenViewModel>()
    LaunchedEffect(true) {
        viewModel.updateTable()
    }

    TakeDateGraph(onBackClick = onBackClick, takeDayGraphData = viewModel.uiState.value.table)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TakeDateGraph(
    onBackClick: () -> Boolean,
    takeDayGraphData: List<Row>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.dose_time_graph))
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                }

            )
        }
    ) { innerPadding ->
        //DrawPointsAndLine(modifier = Modifier.padding(innerPadding))        //Take the last seven days and map it to take
        val scrollState = rememberScrollState()

        val hours = (1..23).toList()

        TimeGraph(
            modifier = modifier
                .wrapContentSize(),
            dayItemsCount = 7,
            hoursHeader = {
                HoursHeader(hours)
            },
            dayLabel = { index -> //Week index
                DayLabel(takeDayGraphData[takeDayGraphData.size-7 + index].date.dayOfWeek)
            },
            bar = {index ->
                val data = takeDayGraphData[takeDayGraphData.size-7 + index]
                // We have access to Modifier.timeGraphBar() as we are now in TimeGraphScope
                TakePoint(
                    sleepData = data,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .timeGraphBar(
                            start = data.firstSleepStart,
                            end = data.lastSleepEnd,
                            hours = hours,
                        ),
                )

            },
            line = {
                // We have access to Modifier.timeGraphBar() as we are now in TimeGraphScope
                val VAPORWAVE_PINK = Color(0xFFFF71CE)
                val VAPORWAVE_BLUE = Color(0xFF01CDFE)
                val lineBrush = remember {
                    Brush.verticalGradient(
                        listOf(VAPORWAVE_PINK, VAPORWAVE_BLUE)
                    )
                }

                val points = remember(takeDayGraphData) {
                    takeDayGraphData.map { it.firstSleepStart }
                }
                TimeGraphLine(
                    points = points,
                    hours = hours,
                    brush = lineBrush,
                    // Optional: add a smooth curve
                    grapher = { a, b, c, d, e, f, g ->
                        quadTo(a,b,c,d)
                        //cubicTo(a,b,c,d,e,f)
                    }
                )
            }
        )
    }
}

data class Take(
    val secondOfDay: Int,
    val triState: List<TriState>
) {
    val color: Color
        get() = when {
            triState.isEmpty() -> Color.White
            triState.all { it == TriState.TAKEN } -> Color.Green
            triState.all { it == TriState.NOTTAKEN } -> Color.Red
            TriState.TAKEN in triState -> Color.Yellow
            else -> Color.White
        }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawPointsAndLine(
    modifier: Modifier = Modifier,
    graphData : List<Take> = fakeGraphData
) {

    Canvas(
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(3 / 2f)
            .fillMaxSize()
            .drawWithCache {
                // generateSmoothPath now returns a Path and the list of points
                val (path, points) = generateSmoothPathWithPoints(graphData, size)

                onDrawBehind {
                    drawPath(
                        path, PathColor,
                        style = Stroke(2.dp.toPx())
                    )

                    // Draw circles for each point
                    points.forEach { pointInfo ->
                        drawCircle(
                            pointInfo.color,
                            radius = 16f,
                            center = Offset(pointInfo.balanceX, pointInfo.balanceY)
                        )
                    }
                }
            }
    ) {
        val barWidthPx = 1.dp.toPx()
        drawRect(BarColor, style = Stroke(barWidthPx))
        val verticalLines = 4
        val verticalSize = size.width / (verticalLines + 1)
        repeat(verticalLines) { i ->
            val startX = verticalSize * (i + 1)
            drawLine(
                BarColor,
                start = Offset(startX, 0f),
                end = Offset(startX, size.height),
                strokeWidth = barWidthPx
            )
        }
        val horizontalLines = 3
        val sectionSize = size.height / (horizontalLines + 1)
        repeat(horizontalLines) { i ->
            val startY = sectionSize * (i + 1)
            drawLine(
                BarColor,
                start = Offset(0f, startY),
                end = Offset(size.width, startY),
                strokeWidth = barWidthPx
            )
        }

    }
}


// A new data class to hold all points for a single curve segment
data class PathPoints(
    val balanceX: Float,
    val balanceY: Float,
    val color: Color
)

fun generateSmoothPathWithPoints(data: List<Take>, size: Size): Pair<Path, List<PathPoints>> {
    val path = Path()
    val points = mutableListOf<PathPoints>()
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries

    val max = data.maxBy { it.secondOfDay }
    val min = data.minBy { it.secondOfDay } // will map to x= 0, y = height
    val range = max.secondOfDay - min.secondOfDay
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY =
        size.height - (data.first().secondOfDay - min.secondOfDay).toFloat() * heightPxPerAmount

    path.moveTo(previousBalanceX, previousBalanceY)

    data.forEachIndexed { i, take ->

        val balanceX = i * weekWidth
        val balanceY = size.height - (take.secondOfDay - min.secondOfDay).toFloat() *
                heightPxPerAmount
        // to do smooth curve graph - we use cubicTo
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
            balanceX, balanceY
        )


        // Store the points for this segment
        points.add(PathPoints(balanceX, balanceY, take.color))

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return Pair(path, points)
}