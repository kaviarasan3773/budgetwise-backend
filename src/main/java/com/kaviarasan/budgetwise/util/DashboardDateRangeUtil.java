package com.kaviarasan.budgetwise.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.kaviarasan.budgetwise.dto.DashboardPeriod;

public class DashboardDateRangeUtil {

    private DashboardDateRangeUtil() {
        // Prevent object creation
    }


    public static DateRange getDateRange(
            DashboardPeriod period,
            LocalDate fromDate,
            LocalDate toDate) {

        LocalDate today = LocalDate.now();

        LocalDate startDate;
        LocalDate endDate;


        switch (period) {

            case TODAY:

                startDate = today;
                endDate = today;

                break;


            case THIS_WEEK:

                startDate =
                        today.with(DayOfWeek.MONDAY);

                endDate =
                        today.with(DayOfWeek.SUNDAY);

                break;


            case LAST_MONTH:

                LocalDate previousMonth =
                        today.minusMonths(1);

                startDate =
                        previousMonth.withDayOfMonth(1);

                endDate =
                        previousMonth.withDayOfMonth(
                                previousMonth.lengthOfMonth());

                break;


            case THIS_YEAR:

                startDate =
                        today.withDayOfYear(1);

                endDate =
                        today.withDayOfYear(
                                today.lengthOfYear());

                break;


            case CUSTOM:

                if (fromDate == null ||
                    toDate == null) {

                    throw new IllegalArgumentException(
                            "From date and To date are required");
                }

                if (fromDate.isAfter(toDate)) {

                    throw new IllegalArgumentException(
                            "From date cannot be after To date");
                }

                startDate = fromDate;
                endDate = toDate;

                break;


            case THIS_MONTH:
            default:

                startDate =
                        today.withDayOfMonth(1);

                endDate =
                        today.withDayOfMonth(
                                today.lengthOfMonth());

                break;
        }


        return new DateRange(
                startDate,
                endDate);
    }
}