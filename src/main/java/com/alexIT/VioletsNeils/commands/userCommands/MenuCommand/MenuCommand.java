package com.alexIT.VioletsNeils.commands.userCommands.MenuCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.AdminKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.DefaultKeyboardBuilder;
import com.alexIT.VioletsNeils.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class MenuCommand implements Command {

    private final UserRoleService userRoleService;
    private final KeyboardBuilder defaultKeyboardBuilder = new DefaultKeyboardBuilder();
    private final KeyboardBuilder adminKeyboardBuilder = new AdminKeyboardBuilder();

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.equalsIgnoreCase("/menu") && state.equals(UserState.PREPARED) && (roleUser.equals(RoleUser.USER) || roleUser.equals(RoleUser.ADMIN));
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        KeyboardBuilder builder;
        if (RoleUser.ADMIN.equals(userRoleService.getRole(userDto.getUserId()))) {
            builder = adminKeyboardBuilder;
        } else {
            builder = defaultKeyboardBuilder;
        }

        InlineKeyboardMarkup keyboard = builder.build();

        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text("Выберите действие.")
                .replyMarkup(keyboard)
                .build();
    }
}
