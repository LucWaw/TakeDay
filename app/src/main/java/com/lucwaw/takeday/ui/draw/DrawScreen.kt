package com.lucwaw.takeday.ui.draw

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.lucwaw.takeday.ui.theme.BarColor
import com.lucwaw.takeday.ui.theme.PathColor

@Composable
fun DrawRoot(onBackClick: () -> Unit) {
    DrawScreen(onBackClick = onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val graphData : List<Balance> = listOf(Balance(1),Balance(3),Balance(4),Balance(2),Balance(3),Balance(5))
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Draw") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
}