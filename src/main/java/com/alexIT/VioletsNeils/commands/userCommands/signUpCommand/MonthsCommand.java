package com.alexIT.VioletsNeils.commands.userCommands.signUpCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.DaysKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.MonthKeyboardBuilder;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
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
    private final DailyRecordServiceImpl dailyRecordService;
    private final UserSessionManager sessionManager;
    private final DaysKeyboardFactory daysKeyboardFactory;
    private boolean isChooseMonthCommand;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        if (text != null && text.equals("/chooseMonth") && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.USER)) {
            isChooseMonthCommand = true;
            return true;
        }
        if (text != null && (text.startsWith("/currentMonth") || text.startsWith("/nextMonth")) && state.equals(UserState.PREPARED) && roleUser.equals(RoleUser.USER)) {
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
        KeyboardBuilder keyboardBuilder = daysKeyboardFactory.create(dailyRecordService, year, currentMonth, "/date_%d-%d-%d", "/chooseMonth");
        InlineKeyboardMarkup keyboard = keyboardBuilder.build();
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день.")
                .replyMarkup(keyboard)
                .build();
    }
}
