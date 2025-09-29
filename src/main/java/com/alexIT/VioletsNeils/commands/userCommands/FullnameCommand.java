package com.alexIT.VioletsNeils.commands.userCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.ConfirmKeyboardBuilder;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class FullnameCommand implements Command {

    private final UserSessionManager sessionManager;
    private String fullName;
    private final ConfirmKeyboardBuilder confirmKeyboardBuilder;
    private static final String INFO = """
            Введите номер телефона
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        if (text != null && text.matches("^[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?\\s[А-ЯЁ][а-яё]+$") && state.equals(UserState.WAIT_NAME) && roleUser.equals(RoleUser.USER)) {
            fullName = text;
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userSession.setFullName(fullName);
        userSession.setState(UserState.WAIT_PHONE);
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text(INFO)
                .build();
    }
}
