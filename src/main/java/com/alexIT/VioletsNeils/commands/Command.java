package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;

public interface Command {

    boolean supports(String text, UserState state, RoleUser roleUser);

    BotApiMethod<?> handler(TgUserDto userDto);
}
