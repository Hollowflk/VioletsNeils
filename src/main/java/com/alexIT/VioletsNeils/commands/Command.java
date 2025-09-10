package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

public interface Command {

    boolean supports(String text);

    BotApiMethod<?> handler(TgUserDto userDto);
}
