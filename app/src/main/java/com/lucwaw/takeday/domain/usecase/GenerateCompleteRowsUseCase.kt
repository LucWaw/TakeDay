package com.lucwaw.takeday.domain.usecase

import com.lucwaw.takeday.domain.model.Row
import java.time.LocalDate
import javax.inject.Inject

class GenerateCompleteRowsUseCase @Inject constructor() {
    /**
     * Creates a list of [Row] objects, filling in any missing dates between the earliest date
     * present in the input `rows` and today's date.
     *
     * If a date within this range is not found in the input `rows`, a new [Row] is created for that date
     * with `null` time and an empty map for medicines.
     *
     * If the input `rows` is empty, it will generate rows from today up to today (i.e., a single row for today).
     *
     * @param rows A list of existing [Row] objects, potentially with missing dates.
     * @return A new list of [Row] objects that includes all dates from the earliest date in the input `rows`
     * (or today if `rows` is empty) up to and including today. Existing rows are preserved, and missing
     * dates are filled with default [Row] objects.
     */
    operator fun invoke(rows: List<Row>): List<Row> {
        val today = LocalDate.now()
        val rowsByDate = rows.associateBy { it.date }
        val startDate = rows.minOfOrNull { it.date } ?: today

        return (0..today.toEpochDay() - startDate.toEpochDay()).reversed() // Include today
            .map { startDate.plusDays(it) }
            .map { currentDate ->
                rowsByDate[currentDate] ?: Row(
                    date = currentDate,
                    time = null,
                    medicines = emptyMap() // No medicine
                )
            }
    }
}