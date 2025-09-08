package com.alexIT.VioletsNeils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class AdminKeyboardBuilder implements KeyboardBuilder {

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(addButton("Записаться", "/signUp"));
        rows.add(addButton("Посмотреть записи", "/showRecords"));
        rows.add(addButton("Посмотреть свободные окна", "/showFreeRecords"));
        rows.add(addButton("Отправить напоминание", "/sendNotification"));
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
