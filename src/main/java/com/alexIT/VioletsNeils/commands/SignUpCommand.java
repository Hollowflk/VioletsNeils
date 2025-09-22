package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.ServiceCategoryKeyboardBuilder;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
public class SignUpCommand implements Command{


    private final ServiceCategoryKeyboardBuilder serviceCategoryKeyboardBuilder;
    private final UserSessionManager sessionManager;

    public SignUpCommand(ServiceCategoryKeyboardBuilder serviceCategoryKeyboardBuilder, UserSessionManager sessionManager) {
        this.serviceCategoryKeyboardBuilder = serviceCategoryKeyboardBuilder;
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean supports(String text, UserState state) {
        return text != null && text.equals("/signUp") && state.equals(UserState.PREPARED);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        sessionManager.getOrCreateSession(userDto.getUserId());
        InlineKeyboardMarkup keyboard = serviceCategoryKeyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите категорию услуги.")
                .replyMarkup(keyboard)
                .build();
    }
}
