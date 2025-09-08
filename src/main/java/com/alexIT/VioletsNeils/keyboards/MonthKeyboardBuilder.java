package com.alexIT.VioletsNeils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

public class MonthKeyboardBuilder implements KeyboardBuilder
{
    @Override
    public InlineKeyboardMarkup build() {
        Map<String, String> monthMap = getMonths();
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(addButton(monthMap.get("currentMonth"), "/currentMonth"));
        rows.add(addButton(monthMap.get("nextMonth"), "/nextMonth"));
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }

    private Map<String, String> getMonths() {
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
}
