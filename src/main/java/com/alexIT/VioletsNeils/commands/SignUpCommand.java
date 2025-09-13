package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.ServiceCategoryKeyboardBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@Component
public class SignUpCommand implements Command{


    private final ServiceCategoryKeyboardBuilder serviceCategoryKeyboardBuilder;

    public SignUpCommand(ServiceCategoryKeyboardBuilder serviceCategoryKeyboardBuilder) {
        this.serviceCategoryKeyboardBuilder = serviceCategoryKeyboardBuilder;
    }

    @Override
    public boolean supports(String text) {
        return text != null && text.equals("/signUp");
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        InlineKeyboardMarkup keyboard = serviceCategoryKeyboardBuilder.build();
        log.info("Клавиатура получена");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите категорию услуги.")
                .replyMarkup(keyboard)
                .build();
    }
}
