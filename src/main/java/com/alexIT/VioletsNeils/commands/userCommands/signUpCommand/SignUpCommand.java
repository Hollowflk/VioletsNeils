package com.alexIT.VioletsNeils.commands.userCommands.signUpCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.ServiceCategoryKeyboard;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.ServiceCategoryKeyboardFactory;
import com.alexIT.VioletsNeils.service.ServiceCategoryService;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignUpCommand implements Command {

    private final ServiceCategoryKeyboardFactory serviceCategoryKeyboardFactory;
    private final ServiceCategoryService serviceCategoryService;
    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && text.equals("/signUp") && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.USER);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        sessionManager.getOrCreateSession(userDto.getUserId());
        KeyboardBuilder keyboardBuilder = serviceCategoryKeyboardFactory.create(serviceCategoryService, "/service_category_%d", "/menu");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите категорию услуги.")
                .replyMarkup(keyboard)
                .build();
    }
}
