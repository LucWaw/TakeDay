package com.lucwaw.takeday.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateUtils {


    companion object {
        /**
         * Converts a LocalDate object to a human-readable date string.
         * If locale is in English (en), the date will be formatted as "Month Day, Year".
         * If locale is in French (fr), the date will be formatted as "Day Month Year".
         */
        fun LocalDate.toHumanDate(): String {
            val pattern = when (Locale.getDefault().language) {
                "fr" -> "dd/MM/yyyy"
                else -> "MMMM d, yyyy"
            }
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            return this.format(formatter)
        }

        fun dateTimeFormater(): DateTimeFormatter {
            return when (Locale.getDefault().language) {
                "fr" -> DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                else -> DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())
            }
        }


    }
}