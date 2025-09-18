package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.keyboards.AdminKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.DefaultKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.service.UserRoleService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MenuCommand implements Command{

    private final UserRoleService userRoleService;
    private final KeyboardBuilder defaultKeyboardBuilder = new DefaultKeyboardBuilder();
    private final KeyboardBuilder adminKeyboardBuilder = new AdminKeyboardBuilder();

    public MenuCommand(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @Override
    public boolean supports(String text) {
        return text != null && text.equalsIgnoreCase("/menu");
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
