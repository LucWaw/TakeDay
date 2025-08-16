package com.lucwaw.takeday.ui.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.lucwaw.takeday.ui.theme.Purple40

@Composable
fun DrawScreen(modifier: Modifier = Modifier) {
    /*Box(
        modifier = Modifier
            .background(Purple40)
    ) {
        Canvas(
            modifier = Modifier
                .padding(8.dp)
                .aspectRatio(3 / 2f)
                .fillMaxSize()
        ) {
            val barWidthPx = 1.dp.toPx()
            drawRect(Color.Green, style = Stroke(barWidthPx))
            val verticalLines = 4
            val verticalSize = size.width / (verticalLines + 1)
            repeat(verticalLines) { i ->
                val startX = verticalSize * (i + 1)
                drawLine(
                    Color.Yellow,
                    start = Offset(startX, 0f),
                    end = Offset(startX, size.height),
                    strokeWidth = barWidthPx
                )
            }
            val horizontalLines = 3
            val sectionSize = size.height / (horizontalLines + 1)

        }

    }*/
    Canvas(modifier = Modifier.fillMaxSize()) {
        val sizeL = size.width /2
        val sizeH = size.height /2
        val sizeOfRectangle = Size(sizeL, sizeH)
        drawRect(
            color = Color.Green,
            topLeft = Offset(x = (size.width - sizeL)/2F, y = (size.height - sizeH) / 2F),
            size = sizeOfRectangle
        )
    }


}