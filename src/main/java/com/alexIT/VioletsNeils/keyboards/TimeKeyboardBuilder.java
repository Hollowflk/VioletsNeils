package com.alexIT.VioletsNeils.keyboards;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
public class TimeKeyboardBuilder implements KeyboardBuilder {

    private final DailyRecordServiceImpl recordService;

    @Setter
    private LocalDate date;

    private static final Map<LocalTime, String> TIME_MAP = new LinkedHashMap<>();

    public TimeKeyboardBuilder(DailyRecordServiceImpl recordService) {
        this.recordService = recordService;
        TIME_MAP.put(LocalTime.of(10, 0), "10:00");
        TIME_MAP.put(LocalTime.of(12, 0), "12:00");
        TIME_MAP.put(LocalTime.of(15, 0), "15:00");
        TIME_MAP.put(LocalTime.of(17, 0), "17:00");
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        Set<LocalTime> occupiedTimes = new HashSet<>();

        Optional<DailyRecord> optionalDailyRecord = recordService.findByDate(date);
        if (optionalDailyRecord.isPresent()) {
            List<TimeSlot> timeSlots = optionalDailyRecord.get().getTimeSlotList();
            for (TimeSlot timeSlot : timeSlots) {
                occupiedTimes.add(timeSlot.getTime());
            }
        }

        for (Map.Entry<LocalTime, String> entry : TIME_MAP.entrySet()) {
            if (!occupiedTimes.contains(entry.getKey())) {
                String[] timeValue = entry.getValue().split(":");
                rows.add(addButton(
                        String.format("на %s", entry.getValue()),
                        String.format("/record_%s", timeValue[0])
                ));
            }
        }

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
