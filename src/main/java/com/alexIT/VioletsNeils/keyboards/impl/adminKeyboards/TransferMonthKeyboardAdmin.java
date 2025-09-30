package com.alexIT.VioletsNeils.keyboards.impl.adminKeyboards;

import com.alexIT.VioletsNeils.keyboards.KeyboardBuilder;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransferMonthKeyboardAdmin implements KeyboardBuilder {

    private final String callbackPrefix;
    private final String backCallbackPrefix;

    public TransferMonthKeyboardAdmin(String callbackPrefix, String backCallbackPrefix) {
        this.callbackPrefix = callbackPrefix;
        this.backCallbackPrefix = backCallbackPrefix;
    }

    @Override
    public InlineKeyboardMarkup build() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        String currentMonthName = MonthsAndDaysUtils.getNameMonth(localDate.getMonth().getValue());
        String nextMonthName = MonthsAndDaysUtils.getNameMonth(localDate.plusMonths(1).getMonth().getValue());
        rows.add(addButton(currentMonthName, "/" + callbackPrefix + "CurrentMonthAdmin"));
        rows.add(addButton(nextMonthName, "/" + callbackPrefix + "NextMonthAdmin"));
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
