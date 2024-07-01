package de.immowealth

import org.junit.jupiter.api.Assertions.assertEquals
import java.util.Calendar
import java.util.Date

/**
 * Custom assertions
 */
class CustomAssert {

    companion object {

        /**
         * Asserts that the calendar dates are equal
         *
         * @param cal1 The first cal
         * @param date The second cal
         */
        fun assertCalendarSame(cal1: Calendar, date: Date) {
            val cal2 = Calendar.getInstance();
            cal2.time = date;
            val str1 = "" +cal1.get(Calendar.DAY_OF_MONTH) + "/" +  cal1.get(Calendar.MONTH) + "/" +cal1.get(Calendar.YEAR);
            val str2 = "" +cal2.get(Calendar.DAY_OF_MONTH) + "/" + cal2.get(Calendar.MONTH) + "/" +  cal2.get(Calendar.YEAR);
            assertEquals(str1, str2)
        }
    }
}