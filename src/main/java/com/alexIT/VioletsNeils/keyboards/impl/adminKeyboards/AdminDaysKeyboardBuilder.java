package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.service.impl.DailyRecordServiceImpl;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminDaysKeyboardBuilder implements KeyboardBuilder {

    @Setter
    private List<DailyRecord> records;

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        InlineKeyboardRow row = new InlineKeyboardRow();
        for (DailyRecord dailyRecord : records) {
            row.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(dailyRecord.getDate()))
                    .callbackData(String.format("/sendNotification_%s", dailyRecord.getDate()))
                    .build());
            if (row.size() == 3) {
                rows.add(row);
                row = new InlineKeyboardRow();
            }
        }
        if (!row.isEmpty()) {
            rows.add(row);
        }
        rows.add(addButton("Назад", "/chooseAdminMonth"));
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
