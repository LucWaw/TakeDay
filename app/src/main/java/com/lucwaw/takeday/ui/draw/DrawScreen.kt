package com.lucwaw.takeday.ui.draw

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.remember
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import com.lucwaw.takeday.ui.theme.BarColor
import com.lucwaw.takeday.ui.theme.PathColor
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3
import kotlin.math.roundToInt
import kotlin.text.get
/*
@Composable
fun DrawRoot(onBackClick: () -> Boolean) {
    TakeDateGraphPrototype(onBackClick = onBackClick)
}


data class Take(
    val date : LocalDate = LocalDate.now(),
    val time : LocalTime = LocalTime.now(), //SleepDayData
    val taken : Boolean = true              //SleepDayData
)




@Composable
private fun TakeDateGraph(onBackClick : () -> Boolean, takeDayGraphData: List<Take>, modifier: Modifier = Modifier) {
    TODO IN first : scaffold with on back click
    val scrollState = rememberScrollState()

    val hours = (1..23).toList()

    TimeGraph(
        modifier = modifier
            .horizontalScroll(scrollState)
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

/*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreenPrototype(
    modifier: Modifier = Modifier,
    onBackClick: () -> Boolean,
) {
    val graphData : List<Balance> = listOf(Balance(1),Balance(3),Balance(4),Balance(2),Balance(3),Balance(5))
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Draw") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Canvas(
            modifier = modifier
                .padding(paddingValues)
                .padding(8.dp)
                .aspectRatio(3 / 2f)
                .fillMaxSize()
                .drawWithCache{
                    // generateSmoothPath now returns a Path and the list of points
                    val (path, points) = generateSmoothPathWithPoints(graphData, size)

                    onDrawBehind {
                        drawPath(path, PathColor,
                            style = Stroke(2.dp.toPx()))

                        // Draw circles for each point
                        points.forEach { pointInfo ->
                            drawCircle(Color.Blue, radius = 16f, center = Offset(pointInfo.balanceX, pointInfo.balanceY))
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
}

data class Balance(val amount: Int)

*/
// A new data class to hold all points for a single curve segment
data class PathPoints(
    val balanceX: Float,
    val balanceY: Float
)

fun generateSmoothPathWithPoints(data: List<Balance>, size: Size): Pair<Path, List<PathPoints>> {
    val path = Path()
    val points = mutableListOf<PathPoints>()
    val numberEntries = data.size - 1
    val weekWidth = size.width / numberEntries

    val max = data.maxBy { it.amount }
    val min = data.minBy { it.amount } // will map to x= 0, y = height
    val range = max.amount - min.amount
    val heightPxPerAmount = size.height / range.toFloat()

    var previousBalanceX = 0f
    var previousBalanceY = size.height - (data.first().amount - min.amount).toFloat() * heightPxPerAmount

    path.moveTo(previousBalanceX, previousBalanceY)

    data.forEachIndexed { i, balance ->
        if (i == 0) {
            // The first point is just for moveTo, we start drawing curves from the second point
            return@forEachIndexed
        }

        val balanceX = i * weekWidth
        val balanceY = size.height - (balance.amount - min.amount).toFloat() *
                heightPxPerAmount
        // to do smooth curve graph - we use cubicTo
        val controlPoint1 = PointF((balanceX + previousBalanceX) / 2f, previousBalanceY)
        val controlPoint2 = PointF((balanceX + previousBalanceX) / 2f, balanceY)
        path.cubicTo(
            controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y,
            balanceX, balanceY
        )

        // Store the points for this segment
        points.add(PathPoints(balanceX, balanceY))

        previousBalanceX = balanceX
        previousBalanceY = balanceY
    }
    return Pair(path, points)
}*/