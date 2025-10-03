package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.DaysKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.TimeKeyboardBuilderFactory;
import com.alexIT.VioletsNeils.service.TimeSlotService;
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
public class SelectTimeToSignupUserCommand implements Command {

    private final TimeKeyboardBuilderFactory timeKeyboardBuilderFactory;
    private final DaysKeyboardFactory daysKeyboardFactory;
    private final DailyRecordServiceImpl dailyRecordService;
    private final TimeSlotService timeSlotService;
    private final UserSessionManager sessionManager;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.startsWith("/signupUserDay_") || text.startsWith("/chooseSignupDay"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        if (userDto.getText().equals("/chooseSignupDay")) {
            return chooseDay(userDto);
        }
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        String[] textArr = userDto.getText().split("_");
        String[] day = textArr[1].split("-");
        LocalDate selectedDay = LocalDate.of(
                Integer.parseInt(day[0]),
                Integer.parseInt(day[1]),
                Integer.parseInt(day[2])
        );
        userSession.setSelectedDate(selectedDay);
        KeyboardBuilder keyboardBuilder = timeKeyboardBuilderFactory.create(dailyRecordService, timeSlotService, selectedDay,
                "/signupTime_%s", "/chooseSignupDay");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите время для записи.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }

    private EditMessageText chooseDay(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        KeyboardBuilder keyboardBuilder = daysKeyboardFactory.create(dailyRecordService, userSession.getSelectedMonth().getYear(), userSession.getSelectedMonth().getMonth(),
                "/signupUserDay_%d-%d-%d", "/chooseSignupMonth");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите день для записи пользователя.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }
}
