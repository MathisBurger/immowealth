package de.mathisburger.util

import java.util.Calendar
import java.util.Date

class DateUtils {

    companion object {
        fun getYearFromDate(date: Date): Int {
            var cal = Calendar.getInstance();
            cal.time = date;
            return cal.get(Calendar.YEAR);
        }
    }
}