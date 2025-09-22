package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class UnknowCommand implements Command {

    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state) {
        return true;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String text = "Неизвестная команда";
        if (userSession.getState().equals(UserState.WAIT_PHONE)) {
            text = "Неверный формат. Номер должен начинаться с 8 и содержать 11 цифр.\n" +
                    "Попробуйте снова:";
        }
        if (userSession.getState().equals(UserState.WAIT_NAME)) {
            text = "Неверный формат ФИО. Попробуйте ещё раз";
        }
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text(text)
                .build();
    }
}
