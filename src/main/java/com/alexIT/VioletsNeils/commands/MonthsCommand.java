package com.alexIT.VioletsNeils.commands;

import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.impl.DaysKeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.MonthKeyboardBuilder;
import com.alexIT.VioletsNeils.repository.DailyRepository;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.Month;

@Component
@RequiredArgsConstructor
public class MonthsCommand implements Command {

    private int year;
    private int month;
    private final DailyRepository dailyRepository;
    private final UserSessionManager sessionManager;
    private boolean isChooseMonthCommand;

    @Override
    public boolean supports(String text, UserState state) {
        if (text != null && text.equals("/chooseMonth") && state.equals(UserState.PREPARED)) {
            isChooseMonthCommand = true;
            return true;
        }
        if (text != null && (text.startsWith("/currentMonth") || text.startsWith("/nextMonth"))) {
            String[] textArr = text.split("_");
            String[] currentDateArray = textArr[1].split("-");
            year = Integer.parseInt(currentDateArray[0]);
            month = Integer.parseInt(currentDateArray[1]);
            return true;
        }
        return false;
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (isChooseMonthCommand) {
            UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
            MonthKeyboardBuilder monthKeyboardBuilder = new MonthKeyboardBuilder();
            monthKeyboardBuilder.setSelectedService(userSession.getSelectedService());
            isChooseMonthCommand = false;
            return EditMessageText.builder()
                    .chatId(userDto.getChatId())
                    .messageId(userDto.getMessageId())
                    .text("Выберите месяц.")
                    .replyMarkup(monthKeyboardBuilder.build())
                    .build();
        }
        Month currentMonth = Month.of(month);
        KeyboardBuilder keyboardBuilder = new DaysKeyboardBuilder(dailyRepository, year, currentMonth);
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день.")
                .replyMarkup(keyboard)
                .build();
    }
}
