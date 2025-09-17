package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
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
    public BotApiMethod<?> handler(TgUserDto userDto) {
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text("Неизвестная команда")
                .build();
    }
}
