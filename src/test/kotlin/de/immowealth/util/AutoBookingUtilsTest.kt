package de.immowealth.util

import de.immowealth.CustomAssert.Companion.assertCalendarSame
import de.immowealth.entity.enum.AutoPayInterval
import org.junit.jupiter.api.*;
import java.util.Calendar;


/**
 * Tests the autobooking utils
 */
class AutoBookingUtilsTest {

    @Test
    fun testGetNextAutoPayIntervalDate_Daily() {
        val today = Calendar.getInstance();
        today.add(Calendar.DATE, 1);
        assertCalendarSame(today, AutoBookingUtils.getNextAutoPayIntervalDate(AutoPayInterval.DAILY));
    }

    @Test
    fun testGetNextAutoPayIntervalDate_Weekly() {
        val cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
            cal.add(Calendar.DATE, 7);
        } else {
            cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
        }
        assertCalendarSame(cal, AutoBookingUtils.getNextAutoPayIntervalDate(AutoPayInterval.WEEKLY));
    }

    @Test
    fun testGetNextAutoPayIntervalDate_Monthly() {
        val cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        assertCalendarSame(cal, AutoBookingUtils.getNextAutoPayIntervalDate(AutoPayInterval.MONTHLY));
    }

    @Test
    fun testGetNextAutoPayIntervalDate_Quarterly() {
        val cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        val month = cal.get(Calendar.MONTH);
        if (month < 3 || month == 12) {
            cal.set(Calendar.MONTH, 3);
        } else if (month < 6) {
            cal.set(Calendar.MONTH, 6);
        } else if (month < 9) {
            cal.set(Calendar.MONTH, 9);
        } else {
            cal.set(Calendar.MONTH, 12);
        }
        assertCalendarSame(cal, AutoBookingUtils.getNextAutoPayIntervalDate(AutoPayInterval.QUARTERLY));
    }

    @Test
    fun testGetNextAutoPayIntervalDate_HalfYearly() {
        val cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        val month = cal.get(Calendar.MONTH);
        if (month < 6 || month == 12) {
            cal.set(Calendar.MONTH, 6);
        } else {
            cal.set(Calendar.MONTH, 12);
        }
        assertCalendarSame(cal, AutoBookingUtils.getNextAutoPayIntervalDate(AutoPayInterval.HALF_YEARLY));
    }

    @Test
    fun testGetNextAutoPayIntervalDate_Yearly() {
        val cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.add(Calendar.YEAR, 1);
        assertCalendarSame(cal, AutoBookingUtils.getNextAutoPayIntervalDate(AutoPayInterval.YEARLY));
    }
}