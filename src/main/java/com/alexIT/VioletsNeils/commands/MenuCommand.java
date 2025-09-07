package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.TgUser;
import com.alexIT.VioletsNeils.roles.RoleUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuCommand implements Command{

    @Override
    public boolean supports(String text) {
        return text != null && text.equalsIgnoreCase("/menu");
    }

    @Override
    public BotApiMethod<?> handler(TgUser tgUser) {
        return SendMessage.builder()
                .chatId(tgUser.getChatId())
                .text("Выберите услугу.")
                .replyMarkup(addKeyboard(tgUser.getRole()))
                .build();
    }

    @Override
    public BotApiMethod<?> createMessage() {
        return null;
    }

    public InlineKeyboardMarkup addKeyboard(RoleUser role) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(addButton("Записаться", "/signUp"));
        rows.add(addButton("Посмотреть записи", "/showRecords"));
        rows.add(addButton("Посмотреть свободные окна", "/showFreeRecords"));
        if (RoleUser.ADMIN.equals(role)) {
            rows.add(addButton("Отправить напоминание", "/sendNotification"));
        }
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
