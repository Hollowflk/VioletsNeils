package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

@Component
public class ServiceCommand implements Command{
    @Override
    public boolean supports(String text) {
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        return null;
    }
}
