package com.alexIT.VioletsNeils.keyboards;

import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;


import java.text.MessageFormat;
import java.time.Month;
import java.util.List;

public class DaysKeyboardBuilder implements KeyboardBuilder {

    private final int year;
    private final Month month;
    private static final String CALLBACK_TEXT = "/date_%d-%d-%d";

    public DaysKeyboardBuilder(int year, Month month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public InlineKeyboardMarkup build() {
        int daysInMonth = MonthsAndDaysUtils.getDaysOfMonth(month, year);
        int monthValue = month.getValue();
        InlineKeyboardRow row1 = new InlineKeyboardRow();
        InlineKeyboardRow row2 = new InlineKeyboardRow();
        InlineKeyboardRow row3 = new InlineKeyboardRow();
        InlineKeyboardRow row4 = new InlineKeyboardRow();
        for (int i = 1; i < daysInMonth + 1; i++) {
            if (i <= 8) {
                row1.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(String.format(CALLBACK_TEXT, year, monthValue, i)).build());
            } else if (i <= 16) {
                row2.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(String.format(CALLBACK_TEXT, year, monthValue, i)).build());
            } else if (i <= 24) {
                row3.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(String.format(CALLBACK_TEXT, year, monthValue, i)).build());
            } else {
                row4.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(String.format(CALLBACK_TEXT, year, monthValue, i)).build());
            }
        }

        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData("/signUp")
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        row1,
                        row2,
                        row3,
                        row4,
                        new InlineKeyboardRow(back)
                )).build();
    }

    @Override
    public InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
