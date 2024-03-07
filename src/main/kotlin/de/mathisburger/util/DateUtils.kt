package de.mathisburger.util

import java.util.Calendar
import java.util.Date

/**
 * All utils that are in relation to dates
 */
class DateUtils {

    companion object {
        /**
         * Gets the year as integer from a date
         *
         * @param date The date
         * @return The year as integer
         */
        fun getYearFromDate(date: Date): Int {
            var cal = Calendar.getInstance();
            cal.time = date;
            return cal.get(Calendar.YEAR);
        }
    }
}