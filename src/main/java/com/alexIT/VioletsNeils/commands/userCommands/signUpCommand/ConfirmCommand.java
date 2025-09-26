package com.alexIT.VioletsNeils.commands.userCommands.signUpCommand;

import com.alexIT.VioletsNeils.commands.Command;
import com.alexIT.VioletsNeils.convector.ConvectorUser;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.enums.UserState;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.service.impl.UserServiceImpl;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConfirmCommand implements Command {

    private static final String SUCCESS_MSG = """
            %s, Вы записались на процедуру %s
            на %s %s в %s
            Мастер Виолетта Вертий
            
            Местоположение: Гостиница «Тихая сосна», 2 этаж, кабинет 206
            Телефон для связи: +7 (951) 769-53-94
            
            До встречи🌸
            """;

    private final UserSessionManager sessionManager;
    private final DailyRecordServiceImpl dailyRecordService;
    private final ConvectorUser convectorUser;
    private final UserServiceImpl userService;
    private final TimeSlotService timeSlotService;

    @Override
    public boolean supports(String text, UserState state) {
        return text != null && text.equals("/confirm") && state.equals(UserState.COMPLETED);
    }

    @Override
    public BotApiMethod<?> handler(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userDto.setFullName(userSession.getFullName());
        userDto.setPhoneNumber(userSession.getPhoneNumber());

        TgUser user = getOrCreateUser(userDto, userSession);
        DailyRecord dailyRecord = getOrCreateDailyRecord(userSession.getSelectedDate());

        createAndAddTimeSlot(dailyRecord, userSession, user);
        dailyRecordService.save(dailyRecord);

        sessionManager.removeSession(user.getUserId());
        return buildSuccessMessage(userDto, userSession);
    }

    private TgUser getOrCreateUser(TgUserDto dto, UserSession session) {
        return userService.findById(session.getUserId())
                .orElseGet(() -> {
                    TgUser newUser = convectorUser.convertDtoInUser(dto);
                    return userService.save(newUser);
                });
    }

    private DailyRecord getOrCreateDailyRecord(LocalDate date) {
        return dailyRecordService.findByDate(date)
                .orElseGet(() -> dailyRecordService.save(new DailyRecord(date)));
    }

    private void createAndAddTimeSlot(DailyRecord record, UserSession session, TgUser user) {
        TimeSlot slot = new TimeSlot(
                session.getSelectedTime(),
                record,
                session.getSelectedService(),
                user
        );
        record.getTimeSlotList().add(slot);
        long duration = getDuration(session.getSelectedService().getDuration()).getSeconds();
        if (duration > 7200 && !session.getSelectedTime().equals(LocalTime.of(17, 0))) {
            TimeSlot anotherSlot = getAnotherSlot(record, session, user);
            record.getTimeSlotList().add(anotherSlot);
        }
    }

    private TimeSlot getAnotherSlot(DailyRecord record, UserSession session, TgUser user) {
        List<LocalTime> timeList = new ArrayList<>(timeSlotService.TIME_MAP.keySet());
        int index = timeList.indexOf(session.getSelectedTime());
        LocalTime nextSlot = timeList.get(index + 1);
        return new TimeSlot(
                nextSlot,
                record,
                session.getSelectedService(),
                user
        );
    }

    private EditMessageText buildSuccessMessage(TgUserDto dto, UserSession userSession) {
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text(createMsg(userSession))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(List.of(
                                new InlineKeyboardRow(
                                        InlineKeyboardButton.builder()
                                                .text("Меню")
                                                .callbackData("/menu")
                                                .build())
                        ))
                        .build())
                .build();
    }

    private String createMsg(UserSession userSession) {
        String monthName = MonthsAndDaysUtils.getNameMonth(userSession.getSelectedDate().getMonth().getValue());
        return String.format(SUCCESS_MSG,
                userSession.getFullName(),
                userSession.getSelectedService().getName(),
                userSession.getSelectedDate().getDayOfMonth(),
                MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                userSession.getSelectedTime());
    }

    private Duration getDuration(String durationString) {
        String hour = durationString.substring(1, 2);
        String minutes = durationString.substring(3, 5);
        String durationFormat = String.format("PT%sH%sM", hour, minutes);
        return Duration.parse(durationFormat);
    }
}
