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

    private MonthsAndDaysUtils() {
    }

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

    public static int getDaysOfMonth(Month month, int year) {
        int monthNumber = month.getValue();
        YearMonth yearMonth = YearMonth.of(year, monthNumber);
        return yearMonth.lengthOfMonth();
    }

    public static String getNameMonth(int month) {
        Month mth = Month.of(month);
        Locale loc = Locale.forLanguageTag("ru");
        return mth.getDisplayName(TextStyle.FULL_STANDALONE, loc);
    }

    public static final Map<String, String> monthGenitiveForms = Map.ofEntries(
            Map.entry("январь", "января"),
            Map.entry("февраль", "февраля"),
            Map.entry("март", "марта"),
            Map.entry("апрель", "апреля"),
            Map.entry("май", "мая"),
            Map.entry("июнь", "июня"),
            Map.entry("июль", "июля"),
            Map.entry("август", "августа"),
            Map.entry("сентябрь", "сентября"),
            Map.entry("октябрь", "октября"),
            Map.entry("ноябрь", "ноября"),
            Map.entry("декабрь", "декабря")
    );

}
