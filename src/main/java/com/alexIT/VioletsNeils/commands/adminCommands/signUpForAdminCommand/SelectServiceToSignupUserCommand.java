package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.ServiceCategoryKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.ServiceKeyboardFactory;
import com.alexIT.VioletsNeils.service.ServiceCategoryService;
import com.alexIT.VioletsNeils.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Component
@RequiredArgsConstructor
public class SelectServiceToSignupUserCommand implements Command {

    private final ServiceCategoryKeyboardFactory serviceCategoryKeyboardFactory;
    private final ServiceCategoryService serviceCategoryService;
    private final ServiceKeyboardFactory serviceKeyboardFactory;
    private final ServiceService serviceService;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.startsWith("/signupCategory_") || text.equals("/chooseCategory"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (userDto.getText().equals("/chooseCategory")) {
            return chooseCategory(userDto);
        }
        int categoryId = Integer.parseInt(userDto.getText().split("_")[1]);
        KeyboardBuilder keyboardBuilder = serviceKeyboardFactory.create(categoryId, serviceService,
                "/signupService_%s", "/chooseCategory");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите услугу.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }

    private EditMessageText chooseCategory(TgUserDto userDto) {
        KeyboardBuilder keyboardBuilder = serviceCategoryKeyboardFactory.create(serviceCategoryService,
                "/signupCategory_%s", "/signupUserFromAdmin");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите категорию.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }
}
