package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.keyboards.AdminKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.DefaultKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MenuCommand implements Command{

    private final KeyboardBuilder defaultKeyboardBuilder = new DefaultKeyboardBuilder();
    private final KeyboardBuilder adminKeyboardBuilder = new AdminKeyboardBuilder();

    @Override
    public boolean supports(String text) {
        return text != null && text.equalsIgnoreCase("/menu");
    }

    @Override
    public BotApiMethod<?> handler(TgUser tgUser) {
        RoleUser role = tgUser.getRole();
        KeyboardBuilder builder;
        if (RoleUser.ADMIN.equals(role)) {
            builder = adminKeyboardBuilder;
        } else {
            builder = defaultKeyboardBuilder;
        }

        InlineKeyboardMarkup keyboard = builder.build();

        return SendMessage.builder()
                .chatId(tgUser.getChatId())
                .text("Выберите услугу.")
                .replyMarkup(keyboard)
                .build();
    }
}
