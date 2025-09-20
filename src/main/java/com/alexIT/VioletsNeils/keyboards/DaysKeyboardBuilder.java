package com.alexIT.VioletsNeils.keyboards;

import com.alexIT.VioletsNeils.entity.DailyRecord;
import com.alexIT.VioletsNeils.repository.DailyRepository;
import com.alexIT.VioletsNeils.utils.MonthsAndDaysUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DaysKeyboardBuilder implements KeyboardBuilder {

    private final DailyRepository repository;
    private final int year;
    private final Month month;
    private static final String CALLBACK_TEXT = "/date_%d-%d-%d";

    public DaysKeyboardBuilder(DailyRepository repository, int year, Month month) {
        this.repository = repository;
        this.year = year;
        this.month = month;
    }

    @Override
    public InlineKeyboardMarkup build() {
        int daysInMonth = MonthsAndDaysUtils.getDaysOfMonth(month, year);
        int monthValue = month.getValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        LocalDate now = LocalDate.now();
        if (month.getValue() > now.getMonth().getValue()) {
            currentDay = 1;
        }

        LocalDate start = LocalDate.of(year, monthValue, 1);
        LocalDate end = LocalDate.of(year, monthValue, daysInMonth);
        List<DailyRecord> records = repository.findAllByDateBetween(start, end);

        Set<LocalDate> fullyBookedDates = records.stream()
                .filter(r -> r.getTimeSlotList().size() >= 4)
                .map(DailyRecord::getDate)
                .collect(Collectors.toSet());

        List<InlineKeyboardRow> rows = new ArrayList<>();
        InlineKeyboardRow row = new InlineKeyboardRow();

        for(int i = currentDay; i <= daysInMonth; i++) {
            LocalDate date = LocalDate.of(year, monthValue, i);
            InlineKeyboardButton button;

            if (fullyBookedDates.contains(date)) {
                button = InlineKeyboardButton.builder()
                        .text("🚫" + i)
                        .callbackData(String.format("/data_busy_%s", date))
                        .build();
            } else {
                button = InlineKeyboardButton.builder()
                        .text(String.valueOf(i))
                        .callbackData(String.format(CALLBACK_TEXT, year, monthValue, i))
                        .build();
            }
            row.add(button);

            if (row.size() == 4) {
                rows.add(row);
                row = new InlineKeyboardRow();
            }
        }
        if (!row.isEmpty()) {
            rows.add(row);
        }

        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Назад")
                .callbackData("/chooseMonth")
                .build();
        rows.add(new InlineKeyboardRow(back));
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    @Override
    public InlineKeyboardRow addButton(String description, String callBack) {
        return new InlineKeyboardRow(InlineKeyboardButton.builder()
                .text(description)
                .callbackData(callBack)
                .build());
    }
}
