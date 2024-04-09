package de.immowealth.util

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

        /**
         * Gets today formatted as date string
         *
         * @return Formatted date string
         */
        fun getTodayFormat(): String {
            var cal = Calendar.getInstance();
            return "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
        }

        /**
         * Converts a date to a calendar
         *
         * @param date The date
         * @return The calendar
         */
        fun dateToCalendar(date: Date): Calendar {
            val cal = Calendar.getInstance();
            cal.time = date;
            return cal;
        }
    }
}