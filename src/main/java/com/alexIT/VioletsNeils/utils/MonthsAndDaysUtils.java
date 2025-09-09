package com.alexIT.VioletsNeils.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class MonthsAndDaysUtils {

    private MonthsAndDaysUtils() {}

    public static Map<String, String> getMonthsAsString() {
        Locale loc = Locale.forLanguageTag("ru");
        Map<String, String> monthMap = new HashMap<>();
        LocalDate localDate = LocalDate.now();

        Month currentMonth = localDate.getMonth();
        Month curMonth = Month.of(currentMonth.getValue());
        monthMap.put("currentMonth", curMonth.getDisplayName(TextStyle.FULL_STANDALONE, loc));

        Month nextMonth = localDate.getMonth().plus(1);
        Month nexMonth = Month.of(nextMonth.getValue());
        monthMap.put("nextMonth", nexMonth.getDisplayName(TextStyle.FULL_STANDALONE, loc));

        return monthMap;
    }

    public static Map<String, Month> getMonthsAsValues() {
        Map<String, Month> monthMap = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        monthMap.put("currentMonth", localDate.getMonth());
        monthMap.put("nextMonth", localDate.getMonth().plus(1));
        return monthMap;
    }

    public static int getDaysOfMonth(Month month) {
        int year = LocalDate.now().getYear();
        int monthNumber = month.getValue();
        YearMonth yearMonth = YearMonth.of(year, monthNumber);
        return yearMonth.lengthOfMonth();
    }
}
