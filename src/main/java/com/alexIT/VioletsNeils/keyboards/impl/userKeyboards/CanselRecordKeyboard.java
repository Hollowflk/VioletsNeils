package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class CanselRecordKeyboard implements KeyboardBuilder {

    @Setter
    private List<TimeSlot> timeSlotList;

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
            rows.add(
                    addButton(
                            String.valueOf(currentRecord.getDailyRecord().getDate()),
                            String.format("/canselRecord_%d", currentRecord.getId())));
        }
        rows.add(addButton("Назад", "/menu"));
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
