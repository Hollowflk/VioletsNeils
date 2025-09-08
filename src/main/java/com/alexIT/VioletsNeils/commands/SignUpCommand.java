package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.TgUser;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.MonthKeyboardBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class SignUpCommand implements Command{
    @Override
    public boolean supports(String text) {
        return text != null && text.equals("/signUp");
    }

    @Override
    public BotApiMethod<?> handler(TgUser tgUser) {
        KeyboardBuilder keyboardBuilder = new MonthKeyboardBuilder();
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(tgUser.getChatId())
                .messageId(tgUser.getMessageId())
                .text("Выберите месяц")
                .replyMarkup(keyboard)
                .build();
    }
}
