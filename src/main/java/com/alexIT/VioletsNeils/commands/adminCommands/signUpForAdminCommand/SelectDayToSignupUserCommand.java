package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory.TransferMonthKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.DaysKeyboardFactory;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SelectDayToSignupUserCommand implements Command {

    private final DaysKeyboardFactory daysKeyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;
    private final TransferMonthKeyboardFactory monthKeyboardFactory;
    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.equals("/signUpCurrentMonthAdmin") || text.equals("/signUpNextMonthAdmin") || text.equals("/chooseSignupMonth"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (userDto.getText().equals("/chooseSignupMonth")) {
            return chooseSignupMonth(userDto);
        }
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        LocalDate month;
        if (userDto.getText().equals("/signUpCurrentMonthAdmin")) {
            month = LocalDate.now();
            userSession.setSelectedMonth(month);
        } else {
            month = LocalDate.now().plusMonths(1);
            userSession.setSelectedMonth(month);
        }
        KeyboardBuilder keyboardBuilder = daysKeyboardFactory.create(dailyRecordService, month.getYear(), month.getMonth(),
                "/signupUserDay_%d-%d-%d", "/chooseSignupMonth");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день для записи пользователя.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }

    private EditMessageText chooseSignupMonth(TgUserDto userDto) {
        KeyboardBuilder keyboardBuilder = monthKeyboardFactory.create("signUp", "/signupUserFromAdmin");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите на какой месяц нужно записать пользователя.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }
}
