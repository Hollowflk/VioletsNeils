package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferMonthKeyboardFactory;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
@RequiredArgsConstructor
public class SelectMonthToSignupUserCommand implements Command {

    private final UserSessionManager sessionManager;
    private final UserServiceImpl userService;
    private final TransferMonthKeyboardFactory monthKeyboardFactory;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.startsWith("/selectUser_")
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        Long userId = Long.parseLong(userDto.getText().split("_")[1]);
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userSession.setSelectedUser(userService.findById(userId).get());
        KeyboardBuilder keyboardBuilder = monthKeyboardFactory.create("signUp", "/signupUserFromAdmin");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите на какой месяц нужно записать пользователя.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }
}