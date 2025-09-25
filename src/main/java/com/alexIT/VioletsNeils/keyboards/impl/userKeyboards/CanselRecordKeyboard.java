package com.alexIT.VioletsNeils.keyboards.impl.userKeyboards;

import com.alexIT.VioletsNeils.entity.TimeSlot;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import lombok.Setter;
import org.springframework.stereotype.Component;
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
        for (TimeSlot timeSlot : timeSlotList) {
            rows.add(
                    addButton(
                            String.valueOf(timeSlot.getDailyRecord().getDate()),
                            String.format("/canselRecord_%d", timeSlot.getId())));
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
