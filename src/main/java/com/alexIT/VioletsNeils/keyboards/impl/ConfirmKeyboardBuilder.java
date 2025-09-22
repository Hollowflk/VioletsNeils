package com.alexIT.VioletsNeils.keyboards.impl;

import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

@Component
public class ConfirmKeyboardBuilder implements KeyboardBuilder {

    @Override
    public InlineKeyboardMarkup build() {
        InlineKeyboardButton yesButton = InlineKeyboardButton.builder()
                .text("Да")
                .callbackData("/confirm")
                .build();

        InlineKeyboardButton noButton = InlineKeyboardButton.builder()
                .text("Нет")
                .callbackData("/signUp")
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(yesButton);
        row.add(noButton);

        return InlineKeyboardMarkup.builder().keyboard(List.of(row)).build();
    }

    @Override
    public InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
