package com.alexIT.VioletsNeils.keyboards;

import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MonthKeyboardBuilder implements KeyboardBuilder
{
    @Override
    public InlineKeyboardMarkup build() {
        Map<String, String> monthMap = MonthsAndDaysUtils.getMonthsAsString();
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(addButton(monthMap.get("currentMonth"), "/currentMonth"));
        rows.add(addButton(monthMap.get("nextMonth"), "/nextMonth"));
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData("/menu")
                .build();
        rows.add(new InlineKeyboardRow(back));
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
