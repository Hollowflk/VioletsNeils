package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.impl.MonthKeyboardBuilder;
import com.alexIT.VioletsNeils.service.ServiceService;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class ServiceCommand implements Command{

    private Long serviceId;
    private final ServiceService serviceService;
    private final UserSessionManager sessionManager;
    private final MonthKeyboardBuilder monthKeyboardBuilder;

    @Override
    public boolean supports(String text) {
        if (text != null && text.startsWith("/service_id")) {
            serviceId = Long.parseLong(text.substring(11));
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession session = sessionManager.getOrCreateSession(userDto.getUserId());
        session.setSelectedService(serviceService.findById(serviceId));
        monthKeyboardBuilder.setSelectedService(session.getSelectedService());
        InlineKeyboardMarkup keyboard = monthKeyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите месяц")
                .replyMarkup(keyboard)
                .build();
    }
}
