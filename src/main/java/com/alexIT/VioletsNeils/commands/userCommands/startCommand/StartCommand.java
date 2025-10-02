package com.alexIT.VioletsNeils.commands.userCommands.startCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.MenuKeyboardBuilder;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final UserSessionManager sessionManager;
    private final UserServiceImpl userService;
    private final MenuKeyboardBuilder menuKeyboardBuilder;
    private static final String WELCOME_MASSAGE = """
            Добро пожаловать!
            (Изменить приветствие)
            Для регистрации введите Фамилию Имя.
            """;
    private static final String HELLO_MESSAGE = """
            Привет %s !
            Готовы записаться ?
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.equals("/start") && (state.equals(UserState.NEW_USER) || state.equals(UserState.PREPARED)) && (roleUser.equals(RoleUser.USER) || roleUser.equals(RoleUser.ADMIN));
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        Optional<TgUser> optionalTgUser = userService.findById(userDto.getUserId());
        String text;
        if (optionalTgUser.isPresent()) {
            TgUser tgUser = optionalTgUser.get();
            text = String.format(HELLO_MESSAGE, optionalTgUser.get().getFullName());
            userSession.setFullName(tgUser.getFullName());
            userSession.setPhoneNumber(tgUser.getPhoneNumber());
            userSession.setState(UserState.PREPARED);
            InlineKeyboardMarkup keyboard = menuKeyboardBuilder.build();
            return SendMessage.builder()
                    .chatId(userDto.getChatId())
                    .text(text)
                    .replyMarkup(keyboard)
                    .build();
        } else {
            text = WELCOME_MASSAGE;
            userSession.setState(UserState.WAIT_NAME);
            return SendMessage.builder()
                    .chatId(userDto.getChatId())
                    .text(text)
                    .build();
        }
    }
}
