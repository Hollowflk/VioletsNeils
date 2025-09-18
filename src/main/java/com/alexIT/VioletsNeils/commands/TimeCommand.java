package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.ConfirmKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Component
public class TimeCommand implements Command{

    private LocalTime timeRecord;
    private final UserSessionManager sessionManager;
    private static final String INFO = """
            Запись на %s %s.
            Время записи %s.
            Услуга: %s
            Цена услуги: %d руб.
            Продолжительность: %s
            Подтвердить запись ?
            """;

    public TimeCommand(UserSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public boolean supports(String text) {
        if (text != null && text.startsWith("/record")) {
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
        KeyboardBuilder keyboardBuilder = new ConfirmKeyboardBuilder();
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(String.format(INFO,
                        userSession.getSelectedDate().getDayOfMonth(),
                        MonthsAndDaysUtils.monthGenitiveForms.get(userSession.getSelectedDate().getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))),
                        userSession.getSelectedTime(),
                        userSession.getSelectedService().getName(),
                        userSession.getSelectedService().getPrice(),
                        userSession.getSelectedService().getDuration()))
                .replyMarkup(keyboard)
                .build();
    }
}
