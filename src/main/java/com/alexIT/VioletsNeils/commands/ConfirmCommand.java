package com.alexIT.VioletsNeils.commands;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmCommand implements Command {

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
        UserSession session = sessionManager.getOrCreateSession(userDto.getUserId());
        userDto.setFullName(session.getFullName());
        userDto.setPhoneNumber(session.getPhoneNumber());

        TgUser user = getOrCreateUser(userDto, session);
        DailyRecord dailyRecord = getOrCreateDailyRecord(session.getSelectedDate());

        createAndAddTimeSlot(dailyRecord, session, user);
        dailyRecordService.save(dailyRecord);

        sessionManager.removeSession(user.getUserId());
        return buildSuccessMessage(userDto);
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
        int duration = Integer.parseInt(session.getSelectedService().getDuration().substring(1, 2));
        if (duration >= 2 && !session.getSelectedTime().equals(LocalTime.of(17, 0))) {
            TimeSlot anotherSlot = getAnotherSlot(record, session, user);
            record.getTimeSlotList().add(anotherSlot);
        }
    }

    private TimeSlot getAnotherSlot(DailyRecord record, UserSession session, TgUser user) {
        List<LocalTime> timeList = new ArrayList<>(timeSlotService.timeMap.keySet());
        int index = timeList.indexOf(session.getSelectedTime());
        LocalTime nextSlot = timeList.get(index + 1);
        return new TimeSlot(
                nextSlot,
                record,
                session.getSelectedService(),
                user
        );
    }

    private EditMessageText buildSuccessMessage(TgUserDto dto) {
        return EditMessageText.builder()
                .chatId(dto.getChatId())
                .messageId(dto.getMessageId())
                .text("Вы успешно записались!")
                .build();
    }
}
