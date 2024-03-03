package de.mathisburger.util

import de.mathisburger.entity.enum.AutoPayInterval
import java.util.Calendar
import java.util.Date

class AutoBookingUtils {

    companion object {
        fun getNextAutoPayIntervalDate(interval: AutoPayInterval): Date {
            val today = Calendar.getInstance();
            val next =  when (interval) {
                AutoPayInterval.DAILY -> this.getNextDay(today)
                AutoPayInterval.WEEKLY -> this.getNextWeeklyDay(today)
                AutoPayInterval.MONTHLY -> this.getNextMonthDay(today)
                AutoPayInterval.QUARTERLY -> this.getNextQuarterlyDay(today)
                AutoPayInterval.HALF_YEARLY -> this.getNextHalfYearlyDay(today)
                AutoPayInterval.YEARLY -> this.getNextYearlyDay(today)
            }
            next.set(Calendar.HOUR_OF_DAY, 0);
            next.set(Calendar.MINUTE, 0);
            next.set(Calendar.SECOND, 0);
            return next.time;
        }

        private fun getNextDay(cal: Calendar): Calendar {
            cal.add(Calendar.DATE, 1);
            return cal;
        }

        private fun getNextWeeklyDay(cal: Calendar): Calendar {
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {
                cal.add(Calendar.DATE, 7);
            } else {
                cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
            }
            return cal;
        }

        private fun getNextMonthDay(cal: Calendar): Calendar {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.MONTH, 1);
            return cal;
        }

        private fun getNextQuarterlyDay(cal: Calendar): Calendar {
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
            return cal;
        }
        private fun getNextHalfYearlyDay(cal: Calendar): Calendar {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            val month = cal.get(Calendar.MONTH);
            if (month < 6 || month == 12) {
                cal.set(Calendar.MONTH, 6);
            } else {
                cal.set(Calendar.MONTH, 12);
            }
            return cal;
        }

        private fun getNextYearlyDay(cal: Calendar): Calendar {
            cal.set(Calendar.DAY_OF_YEAR, 1);
            cal.add(Calendar.YEAR, 1);
            return cal;
        }
    }
}