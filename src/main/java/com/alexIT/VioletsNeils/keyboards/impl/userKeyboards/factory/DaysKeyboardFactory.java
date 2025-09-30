package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.factory;

import com.alexIT.VioletsNeils.keyboards.impl.userKeyboards.DaysKeyboardBuilder;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import org.springframework.stereotype.Component;

import java.time.Month;

@Component
public class DaysKeyboardFactory {
    public DaysKeyboardBuilder create(DailyRecordServiceImpl dailyRecordService, int year, Month month,
                                      String prefix, String backCallbackPrefix) {
        return new DaysKeyboardBuilder(dailyRecordService, year, month, prefix, backCallbackPrefix);
    }
}
