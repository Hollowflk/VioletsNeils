package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.ServiceKeyboardBuilder;
import com.alexIT.VioletsNeils.service.ServiceService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class ServiceCategoryCommand implements Command{

    private int serviceCategoryId;
    private final ServiceService serviceService;

    public ServiceCategoryCommand(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Override
    public boolean supports(String text) {
        if (text != null && text.startsWith("/service_category_")) {
            serviceCategoryId = Integer.parseInt(text.substring(18));
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        KeyboardBuilder keyboardBuilder = new ServiceKeyboardBuilder(serviceCategoryId, serviceService);
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите услугу")
                .replyMarkup(keyboard)
                .build();
    }
}
