package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.ListUsersKeyboard;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitSignUpForAdminCommand implements Command {

    private final UserServiceImpl userService;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.equals("/signupUserFromAdmin") || text.startsWith("/userPage_"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        int page = 0;
        if (userDto.getText().startsWith("/userPage_")) {
            page = Integer.parseInt(userDto.getText().split("_")[1]);
        }
        List<TgUser> allUser = userService.findAll();
        KeyboardBuilder keyboardBuilder = new ListUsersKeyboard(allUser, page, 5, "/userPage_");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите пользователя.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }
}
