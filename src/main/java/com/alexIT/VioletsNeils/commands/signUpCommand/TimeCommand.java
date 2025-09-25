package com.alexIT.VioletsNeils.commands.signUpCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.ConfirmKeyboardBuilder;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class TimeCommand implements Command {

    private LocalTime timeRecord;
    private final UserSessionManager sessionManager;
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
    public boolean supports(String text, UserState state) {
        if (text != null && text.startsWith("/record") && state.equals(UserState.PREPARED)) {
            String[] splitText = text.split("_");
            timeRecord = LocalTime.of(Integer.parseInt(splitText[1]), 0, 0);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userSession.setSelectedTime(timeRecord);
        userSession.setState(UserState.COMPLETED);
        InlineKeyboardMarkup keyboard = confirmKeyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createServiceInfoMessage(userSession))
                .replyMarkup(keyboard)
                .build();
    }

    private String createServiceInfoMessage(UserSession userSession) {
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
