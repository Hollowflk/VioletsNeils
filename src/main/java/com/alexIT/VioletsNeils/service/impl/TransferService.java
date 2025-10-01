package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.session.UserSession;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final DailyRecordServiceImpl dailyRecordService;
    private final TimeSlotService timeSlotService;
    private final TelegramClient telegramClient;
    private static final String INFO_ABOUT_TRANSFER = """
            Уведомление о переносе.
            Здравствуйте %s !🌸
            Оповещение о переносе записи с %s %s %s
            На %s %s %s
            
            Местоположение: Гостиница «Тихая сосна», 2 этаж, кабинет 206
            Телефон для связи: +7 (951) 769-53-94
            До встречи🌸
            """;

    public boolean transferRecord(UserSession userSession) {
        Optional<DailyRecord> optionalDailyRecord = dailyRecordService.findByDate(userSession.getSelectedDate());
        DailyRecord dailyRecord;
        dailyRecord = optionalDailyRecord.orElseGet(() -> dailyRecordService.save(new DailyRecord(userSession.getSelectedDate())));
        Optional<TimeSlot> optionalTimeSlot = timeSlotService.findById(userSession.getSelectedRecordId());
        if (optionalTimeSlot.isPresent()) {
            TimeSlot selectedTimeSlot = optionalTimeSlot.get();
            timeSlotService.deleteById(userSession.getSelectedRecordId());
            TimeSlot transferSlot = new TimeSlot(
                    userSession.getSelectedTime(),
                    dailyRecord,
                    selectedTimeSlot.getService(),
                    selectedTimeSlot.getUser()
            );
            dailyRecord.getTimeSlotList().add(transferSlot);
            dailyRecordService.save(dailyRecord);
            execute(createMessageAboutTransfer(transferSlot, selectedTimeSlot), transferSlot);
            return true;
        }
        return false;
    }

    private void execute(SendMessage msg, TimeSlot timeSlot) {
        try {
            telegramClient.execute(msg);
            Thread.sleep(35);
        } catch (TelegramApiException e) {
            log.warn("Не удалось отправить уведомление пользователю {}: {}", timeSlot.getUser().getUserId(), e.getMessage());
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private SendMessage createMessageAboutTransfer(TimeSlot currentTimeSlot, TimeSlot previousTimeSlot) {
        String previousMonthName = MonthsAndDaysUtils.getNameMonth(previousTimeSlot.getDailyRecord().getDate().getMonth().getValue());
        String currentMonthName = MonthsAndDaysUtils.getNameMonth(currentTimeSlot.getDailyRecord().getDate().getMonth().getValue());
        return SendMessage.builder()
                .chatId(previousTimeSlot.getUser().getUserId())
                .text(String.format(INFO_ABOUT_TRANSFER,
                        previousTimeSlot.getUser().getFullName(),
                        previousTimeSlot.getDailyRecord().getDate().getDayOfMonth(),
                        MonthsAndDaysUtils.monthGenitiveForms.get(previousMonthName),
                        previousTimeSlot.getTime(),
                        currentTimeSlot.getDailyRecord().getDate().getDayOfMonth(),
                        MonthsAndDaysUtils.monthGenitiveForms.get(currentMonthName),
                        currentTimeSlot.getTime()))
                .build();
    }
}
