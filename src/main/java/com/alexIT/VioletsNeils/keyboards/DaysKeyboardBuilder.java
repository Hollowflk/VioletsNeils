package com.alexIT.VioletsNeils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;


import java.text.MessageFormat;
import java.util.List;

public class DaysKeyboardBuilder implements KeyboardBuilder {

    private final int daysInMonth;
    private final String monthName;

    public DaysKeyboardBuilder(int daysInMonth, String monthName) {
        this.daysInMonth = daysInMonth;
        this.monthName = monthName;
    }

    @Override
    public InlineKeyboardMarkup build() {
        InlineKeyboardRow row1 = new InlineKeyboardRow();
        InlineKeyboardRow row2 = new InlineKeyboardRow();
        InlineKeyboardRow row3 = new InlineKeyboardRow();
        InlineKeyboardRow row4 = new InlineKeyboardRow();
        for (int i = 1; i < daysInMonth + 1; i++) {
            if (i <= 8) {
                row1.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(MessageFormat.format("/day{0} {1}", i, monthName)).build());
            } else if (i <= 16) {
                row2.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(MessageFormat.format("/day{0} {1}", i, monthName)).build());
            } else if (i <= 24) {
                row3.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(MessageFormat.format("/day{0} {1}", i, monthName)).build());
            } else {
                row4.add(InlineKeyboardButton.builder().text(String.valueOf(i)).callbackData(MessageFormat.format("/day{0} {1}", i, monthName)).build());
            }
        }

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        row1,
                        row2,
                        row3,
                        row4
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
