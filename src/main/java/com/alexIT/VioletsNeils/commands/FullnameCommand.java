package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.ConfirmKeyboardBuilder;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
@RequiredArgsConstructor
public class FullnameCommand implements Command {

    private final UserSessionManager sessionManager;
    private String fullName;
    private final ConfirmKeyboardBuilder confirmKeyboardBuilder;
    private static final String SERVICE_INFO = """
            Запись на %s %s.
            Время записи %s.
            Услуга: %s
            Цена услуги: %d руб.
            Продолжительность: %s
            ФИО: %s
            Номер телефона: %s
            Подтвердить запись ?
            """;

    @Override
    public boolean supports(String text) {
        if (text != null && text.matches("^[А-ЯЁ][а-яё]+(?:-[А-ЯЁ][а-яё]+)?\\s[А-ЯЁ][а-яё]+$")) {
            fullName = text;
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userSession.setFullName(fullName);
        userSession.setState(UserState.COMPLETED);
        InlineKeyboardMarkup keyboard = confirmKeyboardBuilder.build();
        return SendMessage.builder()
                .chatId(userDto.getChatId())
                .text(createServiceInfoMessage(userSession))
                .replyMarkup(keyboard)
                .build();
    }

    private String createServiceInfoMessage(UserSession userSession)  {
        String monthName = MonthsAndDaysUtils.getNameMonth(userSession.getSelectedDate().getMonth().getValue());
        return String.format(SERVICE_INFO,
                userSession.getSelectedDate().getDayOfMonth(),
                MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                userSession.getSelectedTime(),
                userSession.getSelectedService().getName(),
                userSession.getSelectedService().getPrice(),
                userSession.getSelectedService().getDuration(),
                userSession.getFullName(),
                userSession.getPhoneNumber());
    }
}
