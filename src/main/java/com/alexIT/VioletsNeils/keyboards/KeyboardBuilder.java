package com.alexIT.VioletsNeils.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

public interface KeyboardBuilder {
    InlineKeyboardMarkup build();
    InlineKeyboardRow addButton(String description, String callBack);
}
