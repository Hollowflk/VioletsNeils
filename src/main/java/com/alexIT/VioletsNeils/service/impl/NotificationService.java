package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final DailyRecordServiceImpl dailyRecordService;
    private final TelegramClient telegramClient;
    private static final String NOTIFICATION = """
            Здравствуйте %s!🌸Напоминаю Вам о записи на процедуру %s
            на %s %s в %s
            
            Местоположение: Гостиница «Тихая сосна», 2 этаж, кабинет 206
            Телефон для связи: +7 (951) 769-53-94
            Мастер Виолетта Вертий
            До встречи🌸
            """;
    private static final String NOTIFICATION_TODAY = """
            Здравствуйте %s!🌸Напоминаю Вам о записи на процедуру %s
            на сегодня в %s
            
            Местоположение: Гостиница «Тихая сосна», 2 этаж, кабинет 206
            Телефон для связи: +7 (951) 769-53-94
            Мастер Виолетта Вертий
            До встречи🌸
            """;

    public String sendNotification(LocalDate dateForNotification) {
        Optional<DailyRecord> recordOptional = dailyRecordService.findByDate(dateForNotification);
        if (recordOptional.isPresent()) {
            List<TimeSlot> timeSlotList = recordOptional.get().getTimeSlotList();
            for (TimeSlot timeSlot : timeSlotList) {
                String text = createNotificationText(timeSlot);
                SendMessage msg = SendMessage.builder()
                        .chatId(timeSlot.getUser().getUserId())
                        .text(text)
                        .build();
                execute(msg, timeSlot);
            }
            return "Уведомления отправлены!";
        } else {
            return "Уведомления не были отправлены!";
        }
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

    private String createNotificationText(TimeSlot timeSlot) {
        if (timeSlot.getDailyRecord().getDate().equals(LocalDate.now())) {
            return String.format(NOTIFICATION_TODAY,
                    timeSlot.getUser().getFullName(),
                    timeSlot.getService().getName(),
                    timeSlot.getTime());
        } else {
            String monthName = MonthsAndDaysUtils.getNameMonth(timeSlot.getDailyRecord().getDate().getMonth().getValue());
            return String.format(NOTIFICATION,
                    timeSlot.getUser().getFullName(),
                    timeSlot.getService().getName(),
                    timeSlot.getDailyRecord().getDate().getDayOfMonth(),
                    MonthsAndDaysUtils.monthGenitiveForms.get(monthName),
                    timeSlot.getTime()
            );
        }
    }
}
