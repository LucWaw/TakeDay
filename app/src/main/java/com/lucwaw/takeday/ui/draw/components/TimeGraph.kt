package com.lucwaw.takeday.ui.draw.components

import android.util.Log
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TimeGraph(
    hoursHeader: @Composable () -> Unit,
    dayItemsCount: Int,
    dayLabel: @Composable (index: Int) -> Unit,
    point: @Composable TimeGraphScope.(index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayLabels = @Composable { repeat(dayItemsCount) { dayLabel(it) } }
    val points = @Composable { repeat(dayItemsCount) { TimeGraphScope.point(it) } }
    Layout(
        contents = listOf(dayLabels, hoursHeader, points),
        modifier = modifier.padding(start = 32.dp),
    ) {
            (dayLabelMeasurables, hoursHeaderMeasurables, pointMeasureables),
            constraints,
        ->

        require(hoursHeaderMeasurables.size == 1) {
            "hoursHeader should only emit one composable"
        }
        // The hours header determines the height of our graph timeline
        val hoursHeaderPlaceable = hoursHeaderMeasurables.first().measure(constraints)

        // Day labels are now at the top, measure them individually
        val dayLabelPlaceables = dayLabelMeasurables.map { measurable ->
            measurable.measure(constraints)
        }

        // Each column (day) will have the width of its label
        val dayWidths = dayLabelPlaceables.map { it.width }

        // The points (circles) will determine their own size, so we measure them freely
        val pointPlaceables = pointMeasureables.map { measurable ->
            measurable.measure(constraints)
        }
        Log.d("AAAA", pointMeasureables.toString())
        Log.d("BBBB", dayItemsCount.toString())

        // Total width is the sum of all day label widths plus the width of the hours header on the left
        val totalWidth = dayWidths.sum() + hoursHeaderPlaceable.width

        // Total height is the taller of the hours header or the highest day label
        val totalHeight = maxOf(
            hoursHeaderPlaceable.height,
            dayLabelPlaceables.maxOfOrNull { it.height } ?: 0
        )

        layout(totalWidth, totalHeight) {
            var xPosition = hoursHeaderPlaceable.width

            // Place the hours header on the far left
            hoursHeaderPlaceable.place(x = xPosition /2, y = 0)

            // Place each day's content (label and point) in its own column
            pointPlaceables.forEachIndexed { index, pointPlaceable ->
                val dayLabel = dayLabelPlaceables[index]
                val dayWidth = dayWidths[index]

                // Place the day's label at the top of its column
                dayLabel.place(x = xPosition, y = 0)

                // Get the vertical offset data for the point
                val pointParentData = pointPlaceable.parentData as TimeGraphParentData

                // Calculate the Y position based on the percentage offset and the timeline height
                val yOffset = (pointParentData.yOffsetPercent * hoursHeaderPlaceable.height).roundToInt()

                // To center the point in its column, offset by half the label width minus half the point width
                val centeredX = xPosition + (dayWidth / 2) - (pointPlaceable.width / 2)

                // Place the point
                pointPlaceable.place(x = centeredX, y = yOffset)

                // Move xPosition to the start of the next column
                xPosition += dayWidth
            }
        }
    }
}

@LayoutScopeMarker
@Immutable
object TimeGraphScope {
    @Stable
    // Modifier name and parameters are updated for clarity
    fun Modifier.timeGraphPoint(
        pointOfTime: LocalDateTime,
        hours: List<Int>
    ): Modifier {
        // This logic now calculates a vertical percentage (0.0 to 1.0)
        val earliestTime = LocalTime.of(hours.first(), 0)
        val latestTime = LocalTime.of(hours.last(), 0)

        val totalDurationInMinutes = ChronoUnit.MINUTES.between(earliestTime, latestTime).toFloat()
        val pointTimeInMinutes = ChronoUnit.MINUTES.between(earliestTime, pointOfTime.toLocalTime()).toFloat()

        // Calculate the offset as a percentage of the total duration
        val yOffsetPercent = (pointTimeInMinutes / totalDurationInMinutes).coerceIn(0f, 1f)

        return this.then(
            TimeGraphParentData(
                yOffsetPercent = yOffsetPercent
            )
        )
    }
}

// The ParentData class is now much simpler
class TimeGraphParentData(val yOffsetPercent: Float) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?) = this@TimeGraphParentData
}

