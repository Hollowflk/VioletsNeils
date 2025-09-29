package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransferMonthKeyboardAdmin implements KeyboardBuilder {

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        String currentMonthName = MonthsAndDaysUtils.getNameMonth(localDate.getMonth().getValue());
        String nextMonthName = MonthsAndDaysUtils.getNameMonth(localDate.plusMonths(1).getMonth().getValue());
        rows.add(addButton(currentMonthName, "/transferRecordCurrentMonthAdmin"));
        rows.add(addButton(nextMonthName, "/transferRecordNextMonthAdmin"));
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
