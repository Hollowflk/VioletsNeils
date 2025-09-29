package com.alexIT.VioletsNeils.commands.userCommands;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.MenuKeyboardBuilder;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class PhoneNumberCommand implements Command {

    private final MenuKeyboardBuilder menuKeyboardBuilder;
    private final UserSessionManager sessionManager;
    private String phoneNumber;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        if (text.matches("^8\\d{10}$") && state.equals(UserState.WAIT_PHONE) && roleUser.equals(RoleUser.USER)) {
            phoneNumber = text;
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userSession.setPhoneNumber(phoneNumber);
        userSession.setState(UserState.PREPARED);
        InlineKeyboardMarkup keyboard = menuKeyboardBuilder.build();
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text("Вы успешно зарегистрировались!")
                .replyMarkup(keyboard)
                .build();
    }
}
