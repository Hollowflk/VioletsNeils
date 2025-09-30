package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class TimeKeyboardBuilder implements KeyboardBuilder {

    private final DailyRecordServiceImpl recordService;
    private final TimeSlotService timeSlotService;
    private final LocalDate date;
    private final String callbackPrefix;
    private final String backCallbackPrefix;

    public TimeKeyboardBuilder(DailyRecordServiceImpl recordService, TimeSlotService timeSlotService, LocalDate date,
                               String callbackPrefix, String backCallbackPrefix) {
        this.recordService = recordService;
        this.timeSlotService = timeSlotService;
        this.date = date;
        this.callbackPrefix = callbackPrefix;
        this.backCallbackPrefix = backCallbackPrefix;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        Set<LocalTime> occupiedTimes = new HashSet<>();
        LocalDate today = LocalDate.now();

        Optional<DailyRecord> optionalDailyRecord = recordService.findByDate(date);
        if (optionalDailyRecord.isPresent()) {
            List<TimeSlot> timeSlots = optionalDailyRecord.get().getTimeSlotList();
            for (TimeSlot timeSlot : timeSlots) {
                occupiedTimes.add(timeSlot.getTime());
            }
        }

        for (Map.Entry<LocalTime, String> entry : timeSlotService.TIME_MAP.entrySet()) {

            if (date.equals(today) && entry.getKey().isBefore(LocalTime.now())) {
                continue;
            }

            if (!occupiedTimes.contains(entry.getKey())) {
                String[] timeValue = entry.getValue().split(":");
                rows.add(addButton(
                        String.format("на %s", entry.getValue()),
                        String.format(callbackPrefix, timeValue[0])
                ));
            }
        }

        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(backCallbackPrefix)
                .build();

        rows.add(new InlineKeyboardRow(backButton));
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
