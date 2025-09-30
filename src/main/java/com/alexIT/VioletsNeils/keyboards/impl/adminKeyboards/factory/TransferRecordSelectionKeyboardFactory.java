package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.factory;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards.TransferRecordSelectionKeyboard;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransferRecordSelectionKeyboardFactory {
    public TransferRecordSelectionKeyboard create(List<TimeSlot> timeSlotList, String callbackPrefix,
                                                  String backCallbackPrefix) {
        return new TransferRecordSelectionKeyboard(timeSlotList, callbackPrefix, backCallbackPrefix);
    }
}
