package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TransferRecordSelectionKeyboard implements KeyboardBuilder {

    private final List<TimeSlot> timeSlotList;
    private final String callbackPrefix;
    private final String backCallbackPrefix;

    public TransferRecordSelectionKeyboard(List<TimeSlot> timeSlotList, String callbackPrefix, String backCallbackPrefix) {
        this.timeSlotList = timeSlotList;
        this.callbackPrefix = callbackPrefix;
        this.backCallbackPrefix = backCallbackPrefix;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (int i = 0; i < timeSlotList.size(); i++) {
            TimeSlot currentRecord = timeSlotList.get(i);
            if (i > 0) {
                TimeSlot previousRecord = timeSlotList.get(i - 1);
                if (currentRecord.getService().getId().equals(previousRecord.getService().getId())
                        && currentRecord.getDailyRecord().getDate().equals(previousRecord.getDailyRecord().getDate())) {
                    continue;
                }
            }
            rows.add(addButton(currentRecord.getTime().toString(), String.format(callbackPrefix, currentRecord.getId())));
        }
        rows.add(addButton("Назад", backCallbackPrefix));
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
