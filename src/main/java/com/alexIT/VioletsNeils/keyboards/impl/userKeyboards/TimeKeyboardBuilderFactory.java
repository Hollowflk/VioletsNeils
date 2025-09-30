package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards;

import com.alexIT.VioletsNeils.service.TimeSlotService;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeKeyboardBuilderFactory {
    public TimeKeyboardBuilder create(DailyRecordServiceImpl recordService, TimeSlotService timeSlotService, LocalDate date,
                                      String callbackPrefix, String backCallbackPrefix) {
        return new TimeKeyboardBuilder(recordService, timeSlotService, date, callbackPrefix, backCallbackPrefix);
    }
}
