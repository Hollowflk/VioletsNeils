package com.alexIT.VioletsNeils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TimeKeyboardBuilder implements KeyboardBuilder{
    @Override
    public InlineKeyboardMarkup build() {
        // TODO: Заглушка. Потом переделать.
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(addButton("Окно для записи", "/window1"));
        rows.add(addButton("Окно для записи", "/window2"));
        rows.add(addButton("Окно для записи", "/window3"));
        rows.add(addButton("Окно для записи", "/window4"));
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
