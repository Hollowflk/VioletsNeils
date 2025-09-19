package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.keyboards.DaysKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.TimeKeyboardBuilder;
import com.alexIT.VioletsNeils.repository.DailyRepository;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.LocalDate;
import java.time.Month;

@Component
@RequiredArgsConstructor
public class DayCommand implements Command {

    private static final String INFO = """
            Вы выбрали дату: %d %s.
            Выберите время для записи
            """;
    private final TimeKeyboardBuilder timeKeyboardBuilder;
    private final UserSessionManager sessionManager;
    private final DailyRepository dailyRepository;
    private int year;
    private int month;
    private int day;
    private boolean isChooseDateCommand;

    @Override
    public boolean supports(String text) {
        if (text != null && text.equals("/chooseDate")) {
            isChooseDateCommand = true;
            return true;
        }
        if (text != null && text.startsWith("/date")) {
            String[] splitText = text.split("_");
            String[] date = splitText[1].split("-");
            year = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
            day = Integer.parseInt(date[2]);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (isChooseDateCommand) {
            Month currentMonth = Month.of(month);
            KeyboardBuilder keyboardBuilder = new DaysKeyboardBuilder(dailyRepository, year, currentMonth);
            InlineKeyboardMarkup keyboard = keyboardBuilder.build();
            isChooseDateCommand = false;
            return EditMessageText.builder()
                    .chatId(userDto.getChatId())
                    .messageId(userDto.getMessageId())
                    .text("Выберите день.")
                    .replyMarkup(keyboard)
                    .build();
        }
        String monthName = MonthsAndDaysUtils.getNameMonth(month);
        LocalDate selectedDate = LocalDate.of(year, month, day);
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userSession.setSelectedDate(selectedDate);
        timeKeyboardBuilder.setDate(selectedDate);
        InlineKeyboardMarkup keyboard = timeKeyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(String.format(INFO, day, MonthsAndDaysUtils.monthGenitiveForms.get(monthName)))
                .replyMarkup(keyboard)
                .build();
    }
}
