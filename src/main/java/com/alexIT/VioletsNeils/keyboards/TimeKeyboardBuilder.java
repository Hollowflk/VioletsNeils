package com.alexIT.VioletsNeils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TimeKeyboardBuilder implements KeyboardBuilder{

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(addButton("с 10 до 12", "/window1"));
        rows.add(addButton("с 12 до 14", "/window2"));
        rows.add(addButton("с 15 до 17", "/window3"));
        rows.add(addButton("с 17 до 19", "/window4"));
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
