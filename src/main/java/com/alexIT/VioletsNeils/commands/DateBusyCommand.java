package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class DateBusyCommand implements Command{

    private String busyDate;

    @Override
    public boolean supports(String text, UserState state) {
        if (text != null && text.startsWith("/data_busy_") && state.equals(UserState.PREPARED)) {
            busyDate = text.substring(11);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text(String.format("На дату %s больше нет мест для записи%nВыберите новую дату.", busyDate))
                .build();
    }
}
