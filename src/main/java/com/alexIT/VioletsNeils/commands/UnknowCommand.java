package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.entity.TgUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class UnknowCommand implements Command {
    @Override
    public boolean supports(String text) {
        return true;
    }

    @Override
    public BotApiMethod<?> handler(TgUser tgUser) {
        return SendMessage.builder()
                .chatId(tgUser.getChatId())
                .text("Неизвестная команда")
                .build();
    }
}
