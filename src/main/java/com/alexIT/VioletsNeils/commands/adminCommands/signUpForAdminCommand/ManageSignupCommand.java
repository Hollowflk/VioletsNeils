package com.alexIT.VioletsNeils.commands.adminCommands.signUpForAdminCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.enums.RoleUser;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.ConfirmKeyboardFactory;
import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory.TimeKeyboardBuilderFactory;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class ManageSignupCommand implements Command {

    private final UserSessionManager sessionManager;
    private final ConfirmKeyboardFactory confirmKeyboardFactory;
    private final TimeKeyboardBuilderFactory timeKeyboardBuilderFactory;
    private final DailyRecordServiceImpl dailyRecordService;
    private final TimeSlotService timeSlotService;
    private static final String INFO_ABOUT_RECORD = """
            Имя пользователя: %s
            Номер телефона: %s
            Услуга: %s
            Цена услуги: %s р
            Время записи: %s
            Продолжительность услуги: %s
            
            Подтвердить запись ?
            """;

    @Override
    public boolean supports(String text, UserState state, RoleUser roleUser) {
        return text != null && (text.startsWith("/signupTime_") || text.equals("/chooseSignupTime"))
                && state.equals(UserState.PREPARED)
                && roleUser.equals(RoleUser.ADMIN);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        if (userDto.getText().equals("/chooseSignupTime")) {
            return chooseTime(userDto, userSession);
        }
        String[] textArr = userDto.getText().split("_");
        LocalTime selectedTime = LocalTime.of(
                Integer.parseInt(textArr[1]),
                0
        );
        userSession.setSelectedTime(selectedTime);
        KeyboardBuilder keyboardBuilder = confirmKeyboardFactory.create("/confirmSignUpAdmin",
                "/chooseSignupTime");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text(createMessage(userSession))
                .replyMarkup(keyboardBuilder.build())
                .build();
    }

    private String createMessage(UserSession userSession) {
        return String.format(
                INFO_ABOUT_RECORD,
                userSession.getFullName(),
                userSession.getPhoneNumber(),
                userSession.getSelectedService().getName(),
                userSession.getSelectedService().getPrice(),
                userSession.getSelectedTime(),
                userSession.getSelectedService().getDuration()
        );
    }

    private EditMessageText chooseTime(TgUserDto userDto, UserSession userSession) {
        KeyboardBuilder keyboardBuilder = timeKeyboardBuilderFactory.create(dailyRecordService, timeSlotService, userSession.getSelectedDate(),
                "/signupTime_%s", "/chooseSignupDay");
        return EditMessageText.builder()
                .chatId(userDto.getChatId())
                .messageId(userDto.getMessageId())
                .text("Выберите время для записи.")
                .replyMarkup(keyboardBuilder.build())
                .build();
    }
}
