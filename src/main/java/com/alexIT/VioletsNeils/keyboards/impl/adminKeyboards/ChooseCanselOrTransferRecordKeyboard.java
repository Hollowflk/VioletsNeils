package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChooseCanselOrTransferRecordKeyboard implements KeyboardBuilder {

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(addButton("Перенести запись", "/transferRecord_admin"));
        rows.add(addButton("Отменить запись", "/canselRecord_admin"));
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
