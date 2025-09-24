package com.alexIT.VioletsNeils.service.impl;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
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
    private static final String TEXT = """
            Добрый день! У вас запланирована услуга
            "%s"
            на %s часов
            """;

    public String sendNotification(LocalDate dateForNotification) {
        Optional<DailyRecord> recordOptional = dailyRecordService.findByDate(dateForNotification);
        if (recordOptional.isPresent()) {
            List<TimeSlot> timeSlotList = recordOptional.get().getTimeSlotList();
            for (TimeSlot timeSlot : timeSlotList) {
                SendMessage msg = SendMessage.builder()
                        .chatId(timeSlot.getUser().getUserId())
                        .text(String.format(TEXT, timeSlot.getService().getName(), timeSlot.getTime()))
                        .build();
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
            return "Уведомления отправлены!";
        } else {
            return "Уведомления не были отправлены!";
        }
    }
}
