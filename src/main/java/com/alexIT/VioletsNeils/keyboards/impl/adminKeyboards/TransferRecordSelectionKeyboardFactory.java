package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransferRecordSelectionKeyboardFactory {
    public TransferRecordSelectionKeyboard create(List<TimeSlot> timeSlotList, String callbackPrefix,
                                                  String backCallbackPrefix) {
        return new TransferRecordSelectionKeyboard(timeSlotList, callbackPrefix, backCallbackPrefix);
    }
}
