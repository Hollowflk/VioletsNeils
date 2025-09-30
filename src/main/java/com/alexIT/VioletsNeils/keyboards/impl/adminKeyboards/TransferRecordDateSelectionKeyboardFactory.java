package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransferRecordDateSelectionKeyboardFactory {
    public TransferRecordDateSelectionKeyboard create(DailyRecordServiceImpl dailyRecordService, LocalDate selectedMonth,
                                                      String callbackPrefix, String backCallbackPrefix) {
        return new TransferRecordDateSelectionKeyboard(dailyRecordService, selectedMonth, callbackPrefix, backCallbackPrefix);
    }
}
