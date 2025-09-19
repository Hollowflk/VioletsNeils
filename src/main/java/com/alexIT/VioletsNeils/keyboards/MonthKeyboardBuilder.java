package com.alexIT.VioletsNeils.keyboards;

import com.alexIT.VioletsNeils.entity.Service;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MonthKeyboardBuilder implements KeyboardBuilder {

    @Setter
    private Service selectedService;

    @Override
    public InlineKeyboardMarkup build() {
        Map<String, String> monthMap = MonthsAndDaysUtils.getMonthsAsString();
        List<InlineKeyboardRow> rows = new ArrayList<>();
        LocalDate currentMonth = LocalDate.now();
        LocalDate nextMonth = currentMonth.plusMonths(1);
        rows.add(addButton(monthMap.get("currentMonth"), String.format("/currentMonth_%s", currentMonth)));
        rows.add(addButton(monthMap.get("nextMonth"), String.format("/nextMonth_%s", nextMonth)));
        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData(String.format("/service_category_%d", selectedService.getCategory().getId()))
                .build();
        rows.add(new InlineKeyboardRow(back));
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
