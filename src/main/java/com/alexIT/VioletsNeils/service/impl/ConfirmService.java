package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.convector.ConvectorUser;
import com.alexIT.VioletsNeils.dto.TgUserDto;
import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TgUser;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.session.UserSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmService {

    private final UserSessionManager sessionManager;
    private final DailyRecordServiceImpl dailyRecordService;
    private final ConvectorUser convectorUser;
    private final UserServiceImpl userService;
    private final TimeSlotService timeSlotService;
    private final TelegramClient telegramClient;

    public void confirmRecordForUser(TgUserDto userDto) {
        UserSession userSession = sessionManager.getOrCreateSession(userDto.getUserId());
        userDto.setFullName(userSession.getFullName());
        userDto.setPhoneNumber(userSession.getPhoneNumber());

        TgUser user = getOrCreateUser(userDto, userSession);
        DailyRecord dailyRecord = getOrCreateDailyRecord(userSession.getSelectedDate());

        createAndAddTimeSlot(dailyRecord, userSession, user);
        dailyRecordService.save(dailyRecord);

        sessionManager.removeSession(user.getUserId());
    }

    public void confirmRecordForAdmin(UserSession userSession, SendMessage msg) {
        DailyRecord dailyRecord = getOrCreateDailyRecord(userSession.getSelectedDate());
        createAndAddTimeSlot(dailyRecord, userSession, userSession.getSelectedUser());
        dailyRecordService.save(dailyRecord);
        sessionManager.removeSession(userSession.getUserId());
        sendNotification(userSession.getSelectedUser().getUserId(), msg);
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

    private Duration getDuration(String durationString) {
        String hour = durationString.substring(1, 2);
        String minutes = durationString.substring(3, 5);
        String durationFormat = String.format("PT%sH%sM", hour, minutes);
        return Duration.parse(durationFormat);
    }

    private void sendNotification(Long userId, SendMessage msg) {
        try {
            telegramClient.execute(msg);
        } catch (TelegramApiException e) {
            log.warn("Не удалось отправить уведомление пользователю {}: {}", userId, e.getMessage());
        }
    }
}
