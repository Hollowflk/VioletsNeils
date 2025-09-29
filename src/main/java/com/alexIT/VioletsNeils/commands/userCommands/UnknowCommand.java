package com.alexIT.VioletsNeils.commands.userCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;

@Component
@RequiredArgsConstructor
public class UnknowCommand implements Command {

    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return true;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String text = "Неизвестная команда";
        if (userSession.getState().equals(UserState.NEW_USER)) {
            text = """
                    Прежде чем продолжить Вы должны зарегистрироваться!
                    Введите или нажмите /start
                    """;
        }
        if (userSession.getState().equals(UserState.WAIT_PHONE)) {
            text = "Неверный формат. Номер должен начинаться с 8 и содержать 11 цифр.\n" +
                    "Попробуйте снова:";
        }
        if (userSession.getState().equals(UserState.WAIT_NAME)) {
            text = "Неверный формат ФИО. Попробуйте ещё раз";
        }
        if (text.equals("Неизвестная команда")) {
            return DeleteMessage.builder()
                    .chatId(userDto.getChatId())
                    .messageId(userDto.getMessageId())
                    .build();
        }
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text(text)
                .build();
    }
}
