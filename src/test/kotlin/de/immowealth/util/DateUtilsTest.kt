package de.immowealth.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Calendar

/**
 * Tests all date utils
 */
class DateUtilsTest {

    @Test
    fun testGetYearFromDate() {
        val today = Calendar.getInstance();
        assertEquals(today.get(Calendar.YEAR), DateUtils.getYearFromDate(today.time));
    }

    @Test
    fun testGetTodayFormat() {
        val cal = Calendar.getInstance();
        val format =  "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
        assertEquals(format, DateUtils.getTodayFormat());
    }
}